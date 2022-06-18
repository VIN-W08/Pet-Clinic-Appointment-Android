package com.example.vin.petclinicappointment.data.repository

import android.util.Log
import com.example.vin.petclinicappointment.data.model.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.vin.petclinicappointment.data.network.ServiceScheduleApiService

class ServiceScheduleRepository {
    private val serviceScheduleApiService = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:7284/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ServiceScheduleApiService::class.java)

    suspend fun getServiceScheduleList(serviceId: Int, startSchedule: String): Call<Response<ServiceScheduleListResponse>> {
        val response = serviceScheduleApiService.getServiceScheduleList(serviceId, startSchedule)
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun createServiceSchedule(body: CreateServiceScheduleBody): Call<Response<CreateServiceScheduleResponse>> {
        val response = serviceScheduleApiService.createServiceSchedule(body)
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun getServiceScheduleDetail(serviceScheduleId: Int): Call<Response<GetServiceScheduleDetailResponse>> {
        val response = serviceScheduleApiService.getServiceScheduleDetail(serviceScheduleId)
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun updateServiceSchedule(serviceScheduleId: Int, body: UpdateServiceScheduleBody): Call<Response<UpdateServiceScheduleResponse>> {
        val response = serviceScheduleApiService.updateServiceSchedule(serviceScheduleId, body)
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun deleteServiceSchedule(serviceScheduleId: Int): Call<Response<DeleteServiceScheduleResponse>> {
        val response = serviceScheduleApiService.deleteServiceSchedule(serviceScheduleId)
        if (response.isSuccessful) {
            return Call.Success(response)
        }
        return Call.Error(response)
    }
}