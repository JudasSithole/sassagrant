package com.sassa.scamshield.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sassa.scamshield.domain.model.SecurityEvent
import com.sassa.scamshield.domain.repository.ScamRepository
import com.sassa.scamshield.domain.repository.SecurityStats
import com.sassa.scamshield.domain.usecase.GetSecurityStatsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class HomeUiState(
    val stats: SecurityStats = SecurityStats(
        threatsBlocked = 87,
        suspiciousMessages = 12,
        suspiciousCalls = 6,
        protectionConfidence = 98
    ),
    val recentEvents: List<SecurityEvent> = emptyList(),
    val isLoading: Boolean = true
)

class HomeViewModel(
    private val statsUseCase: GetSecurityStatsUseCase,
    private val scamRepo: ScamRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        statsUseCase(),
        scamRepo.securityEvents
    ) { stats, events ->
        HomeUiState(
            stats = stats,
            recentEvents = events.take(8),
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState(isLoading = true)
    )
}
