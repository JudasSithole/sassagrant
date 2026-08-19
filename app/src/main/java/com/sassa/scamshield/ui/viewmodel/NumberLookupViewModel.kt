package com.sassa.scamshield.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sassa.scamshield.domain.usecase.CheckNumberUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class NumberLookupUiState(
    val phoneNumber: String = "",
    val isChecking: Boolean = false,
    val result: CheckNumberUseCase.NumberLookupResult? = null,
    val error: String? = null
)

class NumberLookupViewModel(
    private val checkNumberUseCase: CheckNumberUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NumberLookupUiState())
    val uiState: StateFlow<NumberLookupUiState> = _uiState

    fun updatePhoneNumber(text: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = text)
    }

    fun checkNumber() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isChecking = true, error = null)
            try {
                val result = checkNumberUseCase(_uiState.value.phoneNumber)
                _uiState.value = _uiState.value.copy(
                    isChecking = false,
                    result = result
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isChecking = false,
                    error = e.message ?: "Check failed"
                )
            }
        }
    }

    fun clearResult() {
        _uiState.value = _uiState.value.copy(result = null, phoneNumber = "")
    }
}
