package com.example.vin.petclinicappointment.data.repository

import com.example.vin.petclinicappointment.data.UserDataStore
import com.example.vin.petclinicappointment.data.model.*
import com.example.vin.petclinicappointment.data.network.CustomerApiService
import com.example.vin.petclinicappointment.data.network.PetClinicApiService
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
        .create(CustomerApiService::class.java)

    val clinicApiService = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:7284/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PetClinicApiService::class.java)

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

    suspend fun loginClinic(body: LoginBody): Call<Response<LoginClinicResponse>> {
        val response = clinicApiService.loginPetClinic(body)
        if(response.isSuccessful) {
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun logout(userRole: String){
        when(userRole) {
            "clinic" -> {
                userDataStore.saveUserId(0)
                userDataStore.saveUserEmail("")
                userDataStore.saveUserName("")
                userDataStore.saveUserImage("")
                userDataStore.saveUserPhoneNum("")
                userDataStore.saveUserAddress("")
                userDataStore.saveUserVillageId(0)
                userDataStore.saveUserLatitude(0.0)
                userDataStore.saveUserLongitude(0.0)
                userDataStore.saveUserRole("")
            }
            "customer" -> {
                userDataStore.saveUserId(0)
                userDataStore.saveUserName("")
                userDataStore.saveUserEmail("")
            }
        }
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

    suspend fun saveUserRole(value: String) =
        userDataStore.saveUserRole(value)

    suspend fun getUserRole() =
        userDataStore.getUserRole()

    suspend fun saveUserImage(value: String) =
        userDataStore.saveUserImage(value)

    suspend fun getUserImage() =
        userDataStore.getUserImage()

    suspend fun saveUserAddress(value: String) =
        userDataStore.saveUserAddress(value)

    suspend fun getUserAddress() =
        userDataStore.getUserAddress()

    suspend fun saveUserPhoneNum(value: String) =
        userDataStore.saveUserPhoneNum(value)

    suspend fun getUserPhoneNum() =
        userDataStore.getUserPhoneNum()

    suspend fun saveUserVillageId(value: Long) =
        userDataStore.saveUserVillageId(value)

    suspend fun getUserVillageId() =
        userDataStore.getUserVillageId()

    suspend fun saveUserLatitude(value: Double) =
        userDataStore.saveUserLatitude(value)

    suspend fun getUserLatitude() =
        userDataStore.getUserLatitude()

    suspend fun saveUserLongitude(value: Double) =
        userDataStore.saveUserLongitude(value)

    suspend fun getUserLongitude() =
        userDataStore.getUserLongitude()
}