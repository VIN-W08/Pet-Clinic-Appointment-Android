package com.example.vin.petclinicappointment.data.repository

import com.example.vin.petclinicappointment.data.model.*
import com.example.vin.petclinicappointment.data.network.ClinicScheduleApiService
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ClinicScheduleRepository {
    private val apiService = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:7284/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ClinicScheduleApiService::class.java)

    suspend fun getClinicScheduleList(clinicId: Int?, day: Int?): Call<Response<GetClinicScheduleListResponse>> {
        val response = apiService.getClinicScheduleList(clinicId, day)
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun getClinicScheduleDetail(clinicScheduleId: Int): Call<Response<GetClinicScheduleDetailResponse>> {
        val response = apiService.getClinicScheduleDetail(clinicScheduleId)
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun addClinicSchedule(body: CreateClinicScheduleBody): Call<Response<CreateClinicScheduleResponse>> {
        val response = apiService.createClinicScheduleList(body)
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun updateClinicScheduleList(clinicScheduleId: Int, body: UpdateClinicScheduleBody): Call<Response<UpdateClinicScheduleResponse>> {
        val response = apiService.updateClinicScheduleList(clinicScheduleId = clinicScheduleId, body = body)
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun deleteClinicScheduleList(clinicScheduleId: Int): Call<Response<DeleteClinicScheduleResponse>> {
        val response = apiService.deleteClinicScheduleList(clinicScheduleId)
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }
}