package com.example.vin.petclinicappointment.data.repository

import com.example.vin.petclinicappointment.data.UserDataStore
import com.example.vin.petclinicappointment.data.model.*
import com.example.vin.petclinicappointment.data.network.ApiService
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDataStore: UserDataStore
) {
    val apiService = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:7284/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    suspend fun login(body: LoginBody): Call<Response<LoginResponse>> {
        val response = apiService.loginCustomer(body)
        if(response.isSuccessful) {
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun registerCustomer(body: RegisterBody): Call<Response<RegisterResponse>>{
        val response = apiService.registerCustomer(body)
        if(response.isSuccessful) {
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun saveUserId(value: Int) =
        userDataStore.saveUserId(value)

    suspend fun getUserId() =
        userDataStore.getUserId()

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
}