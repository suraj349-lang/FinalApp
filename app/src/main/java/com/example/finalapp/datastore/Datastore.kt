package com.example.finalapp.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreUserData(private val context: Context) {

    // to make sure there is only one instance
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("UserNumber")
        val USER_MOB_KEY = stringPreferencesKey("user_number")
    }

    // to get the email
    val getUserNumber: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_MOB_KEY] ?: ""
        }

    // to save the email
    suspend fun saveUserNumber(userNumber: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_MOB_KEY] = userNumber
        }
    }
}