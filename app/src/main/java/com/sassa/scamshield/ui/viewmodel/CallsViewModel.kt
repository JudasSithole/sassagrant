package com.sassa.scamshield.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sassa.scamshield.domain.model.ScamNumber
import com.sassa.scamshield.domain.model.SecurityEvent
import com.sassa.scamshield.domain.model.EventType
import com.sassa.scamshield.domain.repository.ScamRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class CallsUiState(
    val scamNumbers: List<ScamNumber> = emptyList(),
    val callsScreened: Int = 0,
    val suspiciousCalls: Int = 0,
    val blockedCalls: Int = 0,
    val isProtectionActive: Boolean = true,
    val isLoading: Boolean = true
)

class CallsViewModel(
    private val scamRepo: ScamRepository
) : ViewModel() {

    val uiState: StateFlow<CallsUiState> = combine(
        scamRepo.scamNumbers,
        scamRepo.securityEvents
    ) { numbers, events ->
        val screened = events.count { it.eventType.name.contains("CALL") }
        val suspicious = events.count { it.eventType.name.contains("CALL") && it.riskScore >= 50 }
        val blocked = events.count { it.eventType == EventType.CALL_BLOCKED }
        CallsUiState(
            scamNumbers = numbers,
            callsScreened = screened,
            suspiciousCalls = suspicious,
            blockedCalls = blocked,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CallsUiState(isLoading = true)
    )
}
