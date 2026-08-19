package com.sassa.scamshield.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sassa.scamshield.domain.model.ScamReport
import com.sassa.scamshield.domain.repository.ScamRepository
import com.sassa.scamshield.domain.usecase.CheckNumberUseCase
import com.sassa.scamshield.util.ScamCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

data class ReportScamUiState(
    val reportType: ReportType = ReportType.PHONE_NUMBER,
    val phoneNumber: String = "",
    val messageContent: String = "",
    val link: String = "",
    val category: ScamCategory = ScamCategory.SASSA_SCAM,
    val description: String = "",
    val isSubmitting: Boolean = false,
    val isSubmitted: Boolean = false,
    val error: String? = null
)

enum class ReportType(val label: String) {
    PHONE_NUMBER("Phone Number"),
    SMS_MESSAGE("SMS Message"),
    SUSPICIOUS_LINK("Suspicious Link")
}

class ReportScamViewModel(
    private val checkNumberUseCase: CheckNumberUseCase,
    private val scamRepo: ScamRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportScamUiState())
    val uiState: StateFlow<ReportScamUiState> get() = _uiState

    fun updateReportType(type: ReportType) {
        _uiState.value = _uiState.value.copy(reportType = type)
    }

    fun updatePhoneNumber(text: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = text)
    }

    fun updateMessageContent(text: String) {
        _uiState.value = _uiState.value.copy(messageContent = text)
    }

    fun updateLink(text: String) {
        _uiState.value = _uiState.value.copy(link = text)
    }

    fun updateCategory(category: ScamCategory) {
        _uiState.value = _uiState.value.copy(category = category)
    }

    fun updateDescription(text: String) {
        _uiState.value = _uiState.value.copy(description = text)
    }

    fun submitReport() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true, error = null)
            try {
                if (_uiState.value.reportType == ReportType.PHONE_NUMBER && _uiState.value.phoneNumber.isBlank()) {
                    _uiState.value = _uiState.value.copy(isSubmitting = false, error = "Phone number is required")
                    return@launch
                }
                if (_uiState.value.reportType == ReportType.SMS_MESSAGE && _uiState.value.messageContent.isBlank()) {
                    _uiState.value = _uiState.value.copy(isSubmitting = false, error = "Message content is required")
                    return@launch
                }
                if (_uiState.value.reportType == ReportType.SUSPICIOUS_LINK && _uiState.value.link.isBlank()) {
                    _uiState.value = _uiState.value.copy(isSubmitting = false, error = "Link is required")
                    return@launch
                }
                if (_uiState.value.description.isBlank()) {
                    _uiState.value = _uiState.value.copy(isSubmitting = false, error = "Description is required")
                    return@launch
                }

                val report = ScamReport(
                    id = UUID.randomUUID().toString(),
                    phoneNumber = if (_uiState.value.reportType == ReportType.PHONE_NUMBER) _uiState.value.phoneNumber else null,
                    messageContent = if (_uiState.value.reportType == ReportType.SMS_MESSAGE) _uiState.value.messageContent else null,
                    link = if (_uiState.value.reportType == ReportType.SUSPICIOUS_LINK) _uiState.value.link else null,
                    category = _uiState.value.category,
                    description = _uiState.value.description,
                    isVerified = false
                )
                scamRepo.saveScamReport(report)

                if (_uiState.value.reportType == ReportType.PHONE_NUMBER) {
                    checkNumberUseCase.reportNumber(
                        _uiState.value.phoneNumber,
                        _uiState.value.category,
                        _uiState.value.description
                    )
                }

                _uiState.value = _uiState.value.copy(isSubmitting = false, isSubmitted = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isSubmitting = false, error = e.message ?: "Submission failed")
            }
        }
    }

    fun resetSubmission() {
        _uiState.value = ReportScamUiState()
    }
}
