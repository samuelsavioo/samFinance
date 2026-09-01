package com.example.samfinance.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.samfinance.network.TransactionItem
import com.example.samfinance.network.UserProfile
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SessionManager(private val context: Context) {
    private val gson = Gson()

    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val JWT_TOKEN = stringPreferencesKey("jwt_token")
        private val USER_PROFILE = stringPreferencesKey("user_profile")
        private val TRANSACTIONS = stringPreferencesKey("transactions_list")
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] ?: false
    }

    val jwtToken: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[JWT_TOKEN]
    }

    val userProfile: Flow<UserProfile?> = context.dataStore.data.map { preferences ->
        preferences[USER_PROFILE]?.let { json ->
            try {
                gson.fromJson(json, UserProfile::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

    val transactions: Flow<List<TransactionItem>> = context.dataStore.data.map { preferences ->
        preferences[TRANSACTIONS]?.let { json ->
            try {
                val type = object : TypeToken<List<TransactionItem>>() {}.type
                gson.fromJson(json, type) ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        } ?: emptyList()
    }

    suspend fun saveSession(loggedIn: Boolean, token: String? = null) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = loggedIn
            if (token != null) {
                preferences[JWT_TOKEN] = token
            }
        }
    }

    suspend fun saveProfile(profile: UserProfile) {
        val json = gson.toJson(profile)
        context.dataStore.edit { preferences ->
            preferences[USER_PROFILE] = json
        }
    }

    suspend fun saveTransactions(list: List<TransactionItem>) {
        val json = gson.toJson(list)
        context.dataStore.edit { preferences ->
            preferences[TRANSACTIONS] = json
        }
    }

    suspend fun logout() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
