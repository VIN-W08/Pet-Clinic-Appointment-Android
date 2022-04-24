package com.example.vin.petclinicappointment.data.repository

import com.example.vin.petclinicappointment.data.UserDataStore
import com.example.vin.petclinicappointment.data.dummyUser
import com.example.vin.petclinicappointment.data.model.Call
import com.example.vin.petclinicappointment.data.model.User
import com.example.vin.petclinicappointment.data.network.ApiService
import kotlinx.coroutines.delay
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDataStore: UserDataStore
) {
    val apiService = Retrofit.Builder()
        .baseUrl("https://api.geoapify.com/v1/geocode/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    suspend fun login(User: User): Call<User> {
        val response = try {
            /*TODO: Network Request*/
            delay(5000)
            dummyUser
        }catch (e: Exception){
            return Call.Error("Error")
        }
        return Call.Success(response)
    }

    suspend fun saveUserName(value: String) =
        userDataStore.saveUserName(value)

    suspend fun getUserName() =
        userDataStore.getUserName()

    suspend fun saveUserEmail(value: String) =
        userDataStore.saveUserEmail(value)

    suspend fun getUserEmail() =
        userDataStore.getUserEmail()

    suspend fun saveUserPassword(value: String) =
        userDataStore.saveUserPassword(value)

    suspend fun getUserPassword() =
        userDataStore.getUserPassword()

    suspend fun saveUserAddress(value: String) =
        userDataStore.saveUserAddress(value)

    suspend fun getUserAddress() =
        userDataStore.getUserAddress()
}