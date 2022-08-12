package com.example.vin.petclinicappointment.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserDataStore @Inject constructor (
    @ApplicationContext private val context: Context
    ) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user")
        val userIdKey = intPreferencesKey("id")
        val userNameKey = stringPreferencesKey("username")
        val emailKey = stringPreferencesKey("email")
        val passwordKey = stringPreferencesKey("password")
        val roleKey = stringPreferencesKey("role")
    }

    suspend fun saveUserId(value: Int){
        context.dataStore.edit { user ->
            user[userIdKey] = value
        }
    }

    suspend fun getUserId(): Int? {
        val preferences = context.dataStore.data.first()
        return preferences[userIdKey]
    }

    suspend fun saveUserName(value: String){
        context.dataStore.edit { user ->
            user[userNameKey] = value
        }
    }

    suspend fun getUserName(): String? {
       val preferences = context.dataStore.data.first()
        return preferences[userNameKey]
    }

    suspend fun saveUserEmail(value: String){
        context.dataStore.edit { user ->
            user[emailKey] = value
        }
    }

    suspend fun getUserEmail(): String? {
        val preferences = context.dataStore.data.first()
        return preferences[emailKey]
    }

    suspend fun saveUserPassword(value: String){
        context.dataStore.edit { user ->
            user[passwordKey] = value
        }
    }

    suspend fun getUserPassword(): String? {
        val preferences = context.dataStore.data.first()
        return preferences[passwordKey]
    }

    suspend fun saveUserRole(value: String){
        context.dataStore.edit { user ->
            user[roleKey] = value
        }
    }

    suspend fun getUserRole(): String? {
        val preferences = context.dataStore.data.first()
        return preferences[roleKey]
    }
}
