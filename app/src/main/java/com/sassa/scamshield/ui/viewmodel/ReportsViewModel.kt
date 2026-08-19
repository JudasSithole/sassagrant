package com.sassa.scamshield.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sassa.scamshield.domain.model.ScamReport
import com.sassa.scamshield.domain.repository.ScamRepository
import com.sassa.scamshield.util.ScamCategory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ReportsUiState(
    val reports: List<ScamReport> = emptyList(),
    val isLoading: Boolean = true
)

class ReportsViewModel(
    private val scamRepo: ScamRepository
) : ViewModel() {

    val uiState: StateFlow<ReportsUiState> = scamRepo.scamReports
        .map { ReportsUiState(reports = it, isLoading = false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ReportsUiState(isLoading = true)
        )

    fun submitReport(
        phoneNumber: String?,
        messageContent: String?,
        link: String?,
        category: ScamCategory,
        description: String
    ) {
        viewModelScope.launch {
            scamRepo.saveScamReport(
                ScamReport(
                    phoneNumber = phoneNumber,
                    messageContent = messageContent,
                    link = link,
                    category = category,
                    description = description
                )
            )
        }
    }
}
