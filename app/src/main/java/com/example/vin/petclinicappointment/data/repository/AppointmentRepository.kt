package com.example.vin.petclinicappointment.data.repository

import com.example.vin.petclinicappointment.data.model.*
import com.example.vin.petclinicappointment.data.network.AppointmentApiService
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppointmentRepository {
    private val apiService = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:7284/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AppointmentApiService::class.java)

    suspend fun createAppointment(body: CreateAppointmentBody): Call<Response<CreateAppointmentResponse>> {
        val response = apiService.createAppointment(body)
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun getAppointmentList(
        customerId: Int? = null,
        clinicId: Int? = null,
        status: Int? = null,
        finished: Boolean? = null
    ): Call<Response<GetAppointmentListResponse>>{
        val response = apiService.getAppointmentList(
            customerId = customerId,
            clinicId = clinicId,
            status = status,
            finished = finished
        )
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun getAppointmentDetail(id: Int): Call<Response<GetAppointmentDetailResponse>>{
        val response = apiService.getAppointmentDetail(id)
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun updateAppointmentStatus(id: Int, body: UpdateAppointmentStatusBody): Call<Response<UpdateAppointmentStatusResponse>>{
        val response = apiService.updateAppointmentStatus(id, body)
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }
}