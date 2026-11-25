package com.example.marketmind.data.cache

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

val Context.cacheDataStore: DataStore<Preferences> by preferencesDataStore(name = "marketmind_cache")

class LocalCacheManager(private val context: Context) {

    companion object {
        val KEY_CACHE_RISK = stringPreferencesKey("cache_risk_json")
        val KEY_CACHE_REGIME = stringPreferencesKey("cache_regime_json")
        val KEY_CACHE_PORTFOLIO = stringPreferencesKey("cache_portfolio_json")
        val KEY_CACHE_OVERVIEW = stringPreferencesKey("cache_overview_json")
        val KEY_CACHE_ANALYTICS = stringPreferencesKey("cache_analytics_json")
    }

    suspend fun saveCache(key: Preferences.Key<String>, jsonString: String) {
        context.cacheDataStore.edit { preferences ->
            preferences[key] = jsonString
        }
    }

    suspend fun getCache(key: Preferences.Key<String>): String? {
        return context.cacheDataStore.data.map { preferences ->
            preferences[key]
        }.firstOrNull()
    }
}