package com.example.vin.petclinicappointment.data.repository

import com.example.vin.petclinicappointment.data.model.*
import com.example.vin.petclinicappointment.data.network.PetClinicApiService
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PetClinicRepository {

    private val apiService = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:7284/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PetClinicApiService::class.java)

    suspend fun getPetClinicList(name: String? = "", latitude: Double? = null, longitude: Double? = null): Call<Response<PetClinicListResponse>> {
        val response = apiService.getPetClinicList(name, latitude, longitude)
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun getPetClinicDetail(id: Int): Call<Response<PetClinicDetailResponse>>{
        val response = apiService.getPetClinicDetail(id)
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun createPetClinic(
        name: RequestBody,
        email: RequestBody,
        password: RequestBody,
        phoneNum: RequestBody,
        address: RequestBody,
        villageId: RequestBody,
        latitude: RequestBody,
        longitude: RequestBody
    ): Call<Response<RegisterClinicResponse>>{
        val response = apiService.createPetClinic(
            name,
            email,
            password,
            phoneNum,
            address,
            villageId,
            latitude,
            longitude
        )
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }
}