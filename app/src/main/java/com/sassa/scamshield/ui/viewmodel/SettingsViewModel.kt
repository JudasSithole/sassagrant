package com.sassa.scamshield.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sassa.scamshield.domain.repository.SettingsRepository
import com.sassa.scamshield.util.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class SettingsUiState(
    val messageProtectionEnabled: Boolean = true,
    val callProtectionEnabled: Boolean = true,
    val autoAlertsEnabled: Boolean = true,
    val blockHighRiskCalls: Boolean = false,
    val localAnalysisEnabled: Boolean = true,
    val dataSharingEnabled: Boolean = false,
    val isLoading: Boolean = true
)

class SettingsViewModel(
    private val settingsRepo: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            val msg = settingsRepo.getBoolean(Constants.PREFS_KEY_MESSAGE_PROTECTION_ENABLED, true)
            val call = settingsRepo.getBoolean(Constants.PREFS_KEY_CALL_PROTECTION_ENABLED, true)
            val alerts = settingsRepo.getBoolean(Constants.PREFS_KEY_AUTO_ALERTS_ENABLED, true)
            val block = settingsRepo.getBoolean(Constants.PREFS_KEY_BLOCK_HIGH_RISK_CALLS, false)
            _uiState.value = SettingsUiState(
                messageProtectionEnabled = msg,
                callProtectionEnabled = call,
                autoAlertsEnabled = alerts,
                blockHighRiskCalls = block,
                isLoading = false
            )
        }
    }

    fun updateMessageProtection(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepo.setBoolean(Constants.PREFS_KEY_MESSAGE_PROTECTION_ENABLED, enabled)
            _uiState.value = _uiState.value.copy(messageProtectionEnabled = enabled)
        }
    }

    fun updateCallProtection(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepo.setBoolean(Constants.PREFS_KEY_CALL_PROTECTION_ENABLED, enabled)
            _uiState.value = _uiState.value.copy(callProtectionEnabled = enabled)
        }
    }

    fun updateAutoAlerts(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepo.setBoolean(Constants.PREFS_KEY_AUTO_ALERTS_ENABLED, enabled)
            _uiState.value = _uiState.value.copy(autoAlertsEnabled = enabled)
        }
    }

    fun updateBlockHighRiskCalls(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepo.setBoolean(Constants.PREFS_KEY_BLOCK_HIGH_RISK_CALLS, enabled)
            _uiState.value = _uiState.value.copy(blockHighRiskCalls = enabled)
        }
    }
}
