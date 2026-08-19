package com.sassa.scamshield

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sassa.scamshield.data.scanner.ScamDetectionEngine
import com.sassa.scamshield.domain.repository.MessageRepository
import com.sassa.scamshield.domain.repository.ScamRepository
import com.sassa.scamshield.domain.repository.SettingsRepository
import com.sassa.scamshield.domain.usecase.AnalyzeMessageUseCase
import com.sassa.scamshield.domain.usecase.CheckNumberUseCase
import com.sassa.scamshield.domain.usecase.GetSecurityStatsUseCase
import com.sassa.scamshield.ui.viewmodel.HomeViewModel
import com.sassa.scamshield.ui.viewmodel.MessagesViewModel
import com.sassa.scamshield.ui.viewmodel.CallsViewModel
import com.sassa.scamshield.ui.viewmodel.ReportsViewModel
import com.sassa.scamshield.ui.viewmodel.SettingsViewModel
import com.sassa.scamshield.ui.viewmodel.ScanMessageViewModel
import com.sassa.scamshield.ui.viewmodel.NumberLookupViewModel
import com.sassa.scamshield.ui.viewmodel.ReportScamViewModel
import com.sassa.scamshield.ui.viewmodel.MessageAnalysisViewModel

class ScamShieldViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    private val scamRepo: ScamRepository = ServiceLocator.provideScamRepository(context)
    private val messageRepo: MessageRepository = ServiceLocator.provideMessageRepository(context)
    private val settingsRepo: SettingsRepository = ServiceLocator.provideSettingsRepository(context)
    private val engine: ScamDetectionEngine = ServiceLocator.provideScamEngine()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            HomeViewModel::class.java -> HomeViewModel(
                GetSecurityStatsUseCase(scamRepo), scamRepo
            )
            MessagesViewModel::class.java -> MessagesViewModel(messageRepo, scamRepo)
            CallsViewModel::class.java -> CallsViewModel(scamRepo)
            ReportsViewModel::class.java -> ReportsViewModel(scamRepo)
            SettingsViewModel::class.java -> SettingsViewModel(settingsRepo)
            ScanMessageViewModel::class.java -> ScanMessageViewModel(AnalyzeMessageUseCase(messageRepo, scamRepo))
            NumberLookupViewModel::class.java -> NumberLookupViewModel(CheckNumberUseCase(scamRepo, engine))
            ReportScamViewModel::class.java -> ReportScamViewModel(CheckNumberUseCase(scamRepo, engine), scamRepo)
            MessageAnalysisViewModel::class.java -> MessageAnalysisViewModel(messageRepo)
            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        } as T
    }
}
