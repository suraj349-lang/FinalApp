package com.spint.app.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.spint.app.di.UserDataStore
import com.spint.app.model.User
import com.spint.app.utils.UserObject
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StoreUserState @Inject constructor(@UserDataStore private val userDataStore: DataStore<Preferences>) {
    companion object {
        val USER_DATA = stringPreferencesKey("user_data")
    }
    suspend fun saveUserInDataStore(user: User) {
        userDataStore.edit { preferences ->
            preferences[USER_DATA] = Gson().toJson(user)
        }
        UserObject.updateUser(user)
    }
    val getUserFromDataStore: Flow<User?> = userDataStore.data.map { preferences ->
        val json = preferences[USER_DATA]
        if (json != null) Gson().fromJson(json, User::class.java) else null
    }
}