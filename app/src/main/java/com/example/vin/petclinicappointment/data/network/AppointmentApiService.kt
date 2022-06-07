package com.example.vin.petclinicappointment.data.network

import com.example.vin.petclinicappointment.data.model.CreateAppointmentBody
import com.example.vin.petclinicappointment.data.model.CreateAppointmentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AppointmentApiService {
    @POST("appointment")
    suspend fun createAppointment(
        @Body body: CreateAppointmentBody
    ): Response<CreateAppointmentResponse>
}