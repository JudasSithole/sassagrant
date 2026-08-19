package com.sassa.scamshield

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.sassa.scamshield.data.MockDataProvider
import com.sassa.scamshield.data.local.database.ScamShieldDatabase
import com.sassa.scamshield.data.repository.MessageRepositoryImpl
import com.sassa.scamshield.data.repository.ScamRepositoryImpl
import com.sassa.scamshield.data.repository.SettingsRepositoryImpl
import com.sassa.scamshield.data.scanner.DetectionConfig
import com.sassa.scamshield.data.scanner.RuleBasedScamDetector
import com.sassa.scamshield.data.scanner.ScamDetectionEngine
import com.sassa.scamshield.domain.repository.MessageRepository
import com.sassa.scamshield.domain.repository.ScamRepository
import com.sassa.scamshield.domain.repository.SettingsRepository
import com.sassa.scamshield.util.Constants

object ServiceLocator {
    private var database: ScamShieldDatabase? = null
    private var scamRepository: ScamRepository? = null
    private var messageRepository: MessageRepository? = null
    private var settingsRepository: SettingsRepository? = null
    private var scamEngine: ScamDetectionEngine? = null
    private var mockDataProvider: MockDataProvider? = null

    fun provideDatabase(context: Context): ScamShieldDatabase {
        return database ?: synchronized(this) {
            database ?: Room.databaseBuilder(
                context,
                ScamShieldDatabase::class.java,
                Constants.DATABASE_NAME
            ).fallbackToDestructiveMigration().build().also { database = it }
        }
    }

    fun provideScamEngine(): ScamDetectionEngine {
        return scamEngine ?: synchronized(this) {
            scamEngine ?: RuleBasedScamDetector(DetectionConfig()).also { scamEngine = it }
        }
    }

    fun provideScamRepository(context: Context): ScamRepository {
        return scamRepository ?: synchronized(this) {
            val db = provideDatabase(context)
            ScamRepositoryImpl(
                db.scamNumberDao(),
                db.scamReportDao(),
                db.messageAnalysisDao(),
                db.securityEventDao()
            ).also {
                scamRepository = it
                provideMockDataProvider(context)?.seedIfEmpty()
            }
        }
    }

    fun provideMessageRepository(context: Context): MessageRepository {
        return messageRepository ?: synchronized(this) {
            val db = provideDatabase(context)
            val engine = provideScamEngine()
            MessageRepositoryImpl(
                engine,
                db.messageAnalysisDao()
            ).also { messageRepository = it }
        }
    }

    fun provideSettingsRepository(context: Context): SettingsRepository {
        return settingsRepository ?: synchronized(this) {
            val prefs = context.getSharedPreferences(
                Constants.SHARED_PREFS_NAME,
                Context.MODE_PRIVATE
            )
            SettingsRepositoryImpl(prefs).also { settingsRepository = it }
        }
    }

    fun provideMockDataProvider(context: Context): MockDataProvider? {
        return mockDataProvider ?: synchronized(this) {
            val db = provideDatabase(context)
            MockDataProvider(
                db.scamNumberDao(),
                db.scamReportDao(),
                db.messageAnalysisDao(),
                db.securityEventDao()
            ).also { mockDataProvider = it }
        }
    }

    fun clear() {
        database?.close()
        database = null
        scamRepository = null
        messageRepository = null
        settingsRepository = null
        scamEngine = null
        mockDataProvider = null
    }

    fun <T : androidx.lifecycle.ViewModel> provideViewModel(
        context: Context,
        factory: (ScamRepository, MessageRepository, SettingsRepository, ScamDetectionEngine) -> T
    ): T {
        return factory(
            provideScamRepository(context),
            provideMessageRepository(context),
            provideSettingsRepository(context),
            provideScamEngine()
        )
    }
}
