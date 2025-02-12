package com.example.fe.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesManager {
    private const val PREFS_NAME = "AppPrefs"
    private const val ACCESS_TOKEN = "accessToken"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveAccessToken(context: Context, token: String) {
        getPreferences(context).edit().putString(ACCESS_TOKEN, token).apply()
    }

    fun getAccessToken(context: Context): String? {
        return getPreferences(context).getString(ACCESS_TOKEN, null)
    }

    fun clearAccessToken(context: Context) {
        getPreferences(context).edit().remove(ACCESS_TOKEN).apply()
    }
}
