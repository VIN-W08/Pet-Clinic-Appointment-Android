package com.example.vin.petclinicappointment.data.repository

import com.example.vin.petclinicappointment.data.network.PetClinicApiService
import com.example.vin.petclinicappointment.data.model.Call
import com.example.vin.petclinicappointment.data.model.PetClinicListResponse
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
}