package com.esgi.fooders.utils

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
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

    suspend fun updateUsername(newValue: String) {
        this.sessionDataStore.edit { settings ->
            settings[USERNAME] = newValue
        }
    }

    suspend fun readUsername() {
        val usernameReadingFlow: Flow<String?> = sessionDataStore.data
            .map { preferences ->
                preferences[USERNAME] ?: ""
            }
        val store = usernameReadingFlow.first()!!
        Log.d("toto", store)
    }

    suspend fun isUsernameSaved(): Boolean {
        val usernameReadingFlow: Flow<String?> = sessionDataStore.data
            .map { preferences ->
                preferences[USERNAME] ?: ""
            }
        return usernameReadingFlow.first()!!.isNotEmpty()
    }

}
