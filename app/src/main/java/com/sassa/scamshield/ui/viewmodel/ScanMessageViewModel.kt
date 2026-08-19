package com.sassa.scamshield.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sassa.scamshield.domain.model.MessageAnalysis
import com.sassa.scamshield.domain.usecase.AnalyzeMessageUseCase
import com.sassa.scamshield.domain.repository.MessageRepository
import com.sassa.scamshield.domain.repository.ScamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ScanMessageUiState(
    val messageText: String = "",
    val senderText: String = "",
    val isAnalyzing: Boolean = false,
    val analysis: MessageAnalysis? = null,
    val error: String? = null
)

class ScanMessageViewModel(
    private val analyzeMessageUseCase: AnalyzeMessageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScanMessageUiState())
    val uiState: StateFlow<ScanMessageUiState> = _uiState

    fun updateMessage(text: String) {
        _uiState.value = _uiState.value.copy(messageText = text)
    }

    fun updateSender(text: String) {
        _uiState.value = _uiState.value.copy(senderText = text)
    }

    fun analyzeMessage() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isAnalyzing = true, error = null)
            try {
                val result = analyzeMessageUseCase(_uiState.value.messageText, _uiState.value.senderText)
                _uiState.value = _uiState.value.copy(
                    isAnalyzing = false,
                    analysis = result
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isAnalyzing = false,
                    error = e.message ?: "Analysis failed"
                )
            }
        }
    }

    fun reportAnalysis() {
        _uiState.value = _uiState.value.copy(analysis = null, messageText = "", error = null)
    }

    fun clearAnalysis() {
        _uiState.value = _uiState.value.copy(analysis = null)
    }
}
