package com.vourourou.forklife.utils

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("session")

class DataStoreManager @Inject constructor(@ApplicationContext appContext: Context) {

    private val sessionDataStore = appContext.dataStore
    private val USERNAME = stringPreferencesKey("username")
    private val THEME = stringPreferencesKey("theme")
    private val DARK_MODE = stringPreferencesKey("dark_mode")
    private val SCAN_COUNT = intPreferencesKey("scan_count")

    // Flow properties for Compose
    val themeFlow: Flow<String> = sessionDataStore.data.map { preferences ->
        preferences[THEME] ?: "Orange"
    }

    val darkModeFlow: Flow<String> = sessionDataStore.data.map { preferences ->
        preferences[DARK_MODE] ?: "System"
    }

    val usernameFlow: Flow<String> = sessionDataStore.data.map { preferences ->
        preferences[USERNAME] ?: ""
    }

    val scanCountFlow: Flow<Int> = sessionDataStore.data.map { preferences ->
        preferences[SCAN_COUNT] ?: 0
    }

    suspend fun updateUsername(newValue: String) {
        this.sessionDataStore.edit { settings ->
            settings[USERNAME] = newValue
        }
    }

    suspend fun updateTheme(newValue: String) {
        this.sessionDataStore.edit { settings ->
            settings[THEME] = newValue
        }
    }

    suspend fun updateDarkMode(newValue: String) {
        this.sessionDataStore.edit { settings ->
            settings[DARK_MODE] = newValue
        }
    }

    suspend fun readDarkMode(): String {
        return darkModeFlow.first()
    }

    suspend fun readUsername(): String {
        val usernameReadingFlow: Flow<String?> = sessionDataStore.data
            .map { preferences ->
                preferences[USERNAME] ?: ""
            }
        val store = usernameReadingFlow.first()!!
        Log.d("toto", store)

        return store
    }

    suspend fun readTheme(): String {
        val themeReadingFlow: Flow<String?> = sessionDataStore.data
            .map { preferences ->
                preferences[THEME] ?: "Orange"
            }
        val store = themeReadingFlow.first()!!

        return store
    }

    suspend fun isUsernameSaved(): Boolean {
        val usernameReadingFlow: Flow<String?> = sessionDataStore.data
            .map { preferences ->
                preferences[USERNAME] ?: ""
            }
        return usernameReadingFlow.first()!!.isNotEmpty()
    }

    suspend fun incrementScanCount() {
        this.sessionDataStore.edit { settings ->
            val currentCount = settings[SCAN_COUNT] ?: 0
            settings[SCAN_COUNT] = currentCount + 1
        }
    }

    suspend fun getScanCount(): Int {
        return scanCountFlow.first()
    }

}
