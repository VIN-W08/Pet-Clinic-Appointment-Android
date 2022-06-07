package com.example.vin.petclinicappointment.data.repository

import android.util.Log
import com.example.vin.petclinicappointment.data.model.Call
import com.example.vin.petclinicappointment.data.model.CreateAppointmentBody
import com.example.vin.petclinicappointment.data.model.CreateAppointmentResponse
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
        Log.d("debug1", "response:${response.body()}")
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }
}