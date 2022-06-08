package com.example.vin.petclinicappointment.data.network

import com.example.vin.petclinicappointment.data.model.CreateAppointmentBody
import com.example.vin.petclinicappointment.data.model.CreateAppointmentResponse
import com.example.vin.petclinicappointment.data.model.GetAppointmentDetailResponse
import com.example.vin.petclinicappointment.data.model.GetAppointmentListResponse
import retrofit2.Response
import retrofit2.http.*

interface AppointmentApiService {
    @POST("appointment")
    suspend fun createAppointment(
        @Body body: CreateAppointmentBody
    ): Response<CreateAppointmentResponse>

    @GET("appointment/list")
    suspend fun getAppointmentList(
        @Query("customer_id") customerId: Int?,
        @Query("clinic_id") clinicId: Int?,
        @Query("status") status: Int?,
        @Query("finished") finished: Boolean?
    ): Response<GetAppointmentListResponse>

    @GET("appointment/{id}")
    suspend fun getAppointmentDetail(
        @Path("id") appointmentId: Int
    ): Response<GetAppointmentDetailResponse>
}