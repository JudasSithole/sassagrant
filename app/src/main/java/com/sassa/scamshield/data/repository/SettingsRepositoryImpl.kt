package com.sassa.scamshield.data.repository

import android.content.SharedPreferences
import com.sassa.scamshield.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val prefs: SharedPreferences
) : SettingsRepository {
    private val editor get() = prefs.edit()

    override suspend fun getBoolean(key: String, default: Boolean): Boolean =
        prefs.getBoolean(key, default)

    override suspend fun setBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value).apply()
    }

    override suspend fun getString(key: String, default: String): String =
        prefs.getString(key, default) ?: default

    override suspend fun setString(key: String, value: String) {
        editor.putString(key, value).apply()
    }

    override suspend fun getInt(key: String, default: Int): Int =
        prefs.getInt(key, default)

    override suspend fun setInt(key: String, value: Int) {
        editor.putInt(key, value).apply()
    }

    override suspend fun clearAll() {
        editor.clear().apply()
    }
}