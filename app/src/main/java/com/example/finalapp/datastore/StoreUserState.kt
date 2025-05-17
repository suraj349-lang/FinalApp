package com.example.finalapp.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.finalapp.di.LoginDataStore
import com.example.finalapp.di.UserDataStore
import com.example.finalapp.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StoreUserState @Inject constructor(@UserDataStore private val userDataStore:  DataStore<Preferences>) {

    // to make sure there is only one instance
    companion object {
        val USER_DATA= stringPreferencesKey("user_data")
    }


    suspend fun saveUserInDataStore(user: User) {
        userDataStore.edit { preferences ->
            preferences[USER_DATA] = user.toString()
        }
    }
    val getUserFromDataStore:Flow<String?> =userDataStore.data.map { preferences->
        preferences[USER_DATA]
    }

}