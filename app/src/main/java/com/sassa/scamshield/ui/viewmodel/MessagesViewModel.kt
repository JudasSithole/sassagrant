package com.sassa.scamshield.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sassa.scamshield.domain.model.MessageAnalysis
import com.sassa.scamshield.domain.repository.MessageRepository
import com.sassa.scamshield.domain.repository.ScamRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class MessagesUiState(
    val analyses: List<MessageAnalysis> = emptyList(),
    val totalAnalyzed: Int = 0,
    val suspiciousCount: Int = 0,
    val blockedCount: Int = 0,
    val safeCount: Int = 0,
    val isProtectionActive: Boolean = true,
    val isLoading: Boolean = true
)

class MessagesViewModel(
    private val messageRepo: MessageRepository,
    private val scamRepo: ScamRepository
) : ViewModel() {

    val uiState: StateFlow<MessagesUiState> = messageRepo.getAllAnalyses()
        .map { analyses ->
            val suspicious = analyses.count { it.riskScore >= 50 }
            val blocked = analyses.count { it.riskScore >= 75 }
            val safe = analyses.count { it.riskScore < 50 }
            MessagesUiState(
                analyses = analyses,
                totalAnalyzed = analyses.size,
                suspiciousCount = suspicious,
                blockedCount = blocked,
                safeCount = safe,
                isLoading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MessagesUiState(isLoading = true)
        )
}
