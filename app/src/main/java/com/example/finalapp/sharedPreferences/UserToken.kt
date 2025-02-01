package com.example.finalapp.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

object SecureStorage {
    private const val PREFS_NAME = "secure_prefs"
    private const val TOKEN_KEY = "auth_token"

    private fun getEncryptedSharedPreferences(context: Context): SharedPreferences {
        return EncryptedSharedPreferences.create(
            PREFS_NAME,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveAuthToken(context: Context, token: String) {
        getEncryptedSharedPreferences(context).edit()
            .putString(TOKEN_KEY, token)
            .apply()
    }

    fun getAuthToken(context: Context): String? {
        return getEncryptedSharedPreferences(context).getString(TOKEN_KEY, null)
    }

    fun clearAuthToken(context: Context) {
        getEncryptedSharedPreferences(context).edit().remove(TOKEN_KEY).apply()
    }
}
