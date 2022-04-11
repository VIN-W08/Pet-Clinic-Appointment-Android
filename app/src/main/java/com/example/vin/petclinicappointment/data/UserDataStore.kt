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
        val userNameKey = stringPreferencesKey("username")
        val emailKey = stringPreferencesKey("email")
        val passwordKey = stringPreferencesKey("password")
        val addressKey = stringPreferencesKey("address")
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

    suspend fun saveUserAddress(value: String){
        context.dataStore.edit { user ->
            user[addressKey] = value
        }
    }

    suspend fun getUserAddress(): String? {
        val preferences = context.dataStore.data.first()
        return preferences[addressKey]
    }
}
