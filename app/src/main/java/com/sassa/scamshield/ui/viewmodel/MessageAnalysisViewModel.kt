package com.sassa.scamshield.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sassa.scamshield.domain.model.MessageAnalysis
import com.sassa.scamshield.domain.repository.MessageRepository
import com.sassa.scamshield.domain.repository.ScamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class MessageAnalysisUiState(
    val analysis: MessageAnalysis? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

class MessageAnalysisViewModel(
    private val messageRepo: MessageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessageAnalysisUiState())
    val uiState: StateFlow<MessageAnalysisUiState> = _uiState

    fun loadAnalysis(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load analysis"
                )
            }
        }
    }
}
