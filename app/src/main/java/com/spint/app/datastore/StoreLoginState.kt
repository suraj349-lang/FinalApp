package com.spint.app.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.spint.app.di.LoginDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StoreLoginState @Inject constructor(@LoginDataStore private val loginDataStore: DataStore<Preferences>) {

    // to make sure there is only one instance
    companion object {
        val USER_ON_BOARDING_KEY = booleanPreferencesKey("user_on_boarding_state")
        val USER_TOKEN= stringPreferencesKey("user_token")
        val LOGIN_STATE= booleanPreferencesKey("login_state")
    }

    // to get the onboarding state
    val getOnBoardingState: Flow<Boolean?> = loginDataStore.data
        .map { preferences ->
            preferences[USER_ON_BOARDING_KEY] ?: false
        }

    // to save the onboarding state
    suspend fun saveOnBoardingState(completed: Boolean) {
        loginDataStore.edit { preferences ->
            preferences[USER_ON_BOARDING_KEY] = completed
        }
    }

    suspend fun saveUserToken(token: String) {
        loginDataStore.edit { preferences ->
            preferences[USER_TOKEN] = token
        }
    }
    suspend fun getTokenOnce(): String {
        return loginDataStore.data.first()[USER_TOKEN] ?: ""
    }


    suspend fun saveLoginState(state: Boolean) {
        loginDataStore.edit { preferences ->
            preferences[LOGIN_STATE] = state
        }
    }
    val getLoginState:Flow<Boolean?> =loginDataStore.data.map { preferences->
        preferences[LOGIN_STATE]
    }

}