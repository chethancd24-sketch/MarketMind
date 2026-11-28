package com.example.marketmind.data.cache

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "marketmind_settings")

class SettingsDataStore(private val context: Context) {

    companion object {
        const val DEFAULT_BASE_URL = "http://10.0.2.2:8000/"
        const val USB_REVERSED_BASE_URL = "http://127.0.0.1:8000/"
        val KEY_BASE_URL = stringPreferencesKey("base_url")
        val KEY_AUTO_REFRESH = booleanPreferencesKey("auto_refresh")
        val KEY_DEMO_MODE = booleanPreferencesKey("demo_mode")

        internal fun resolveBaseUrl(
            savedUrl: String?,
            isDebuggable: Boolean,
            isEmulator: Boolean
        ): String {
            val configuredUrl = savedUrl ?: DEFAULT_BASE_URL
            return if (
                isDebuggable &&
                !isEmulator &&
                configuredUrl.trimEnd('/') == DEFAULT_BASE_URL.trimEnd('/')
            ) {
                USB_REVERSED_BASE_URL
            } else {
                configuredUrl
            }
        }
    }

    val baseUrl: Flow<String> = context.settingsDataStore.data.map { preferences ->
        resolveBaseUrl(
            savedUrl = preferences[KEY_BASE_URL],
            isDebuggable = context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0,
            isEmulator = Build.FINGERPRINT.startsWith("generic") ||
                Build.MODEL.contains("Emulator", ignoreCase = true) ||
                Build.HARDWARE == "goldfish" ||
                Build.HARDWARE == "ranchu"
        )
    }

    val autoRefresh: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[KEY_AUTO_REFRESH] ?: false
    }

    val demoMode: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[KEY_DEMO_MODE] ?: false
    }

    suspend fun saveBaseUrl(url: String) {
        context.settingsDataStore.edit { preferences ->
            val formatted = if (url.endsWith("/")) url else "$url/"
            preferences[KEY_BASE_URL] = formatted
        }
    }

    suspend fun saveAutoRefresh(enabled: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[KEY_AUTO_REFRESH] = enabled
        }
    }

    suspend fun saveDemoMode(enabled: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[KEY_DEMO_MODE] = enabled
        }
    }
}