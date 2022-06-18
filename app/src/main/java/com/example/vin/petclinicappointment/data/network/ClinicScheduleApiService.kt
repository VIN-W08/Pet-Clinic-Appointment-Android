package com.example.vin.petclinicappointment.data.network

import com.example.vin.petclinicappointment.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ClinicScheduleApiService {
    @GET("clinic/schedule/list")
    suspend fun getClinicScheduleList(
        @Query("clinic_id") clinicId: Int?,
        @Query("day") day: Int?
    ): Response<GetClinicScheduleListResponse>

    @GET("clinic/schedule/{id}")
    suspend fun getClinicScheduleDetail(
        @Path("id") clinicScheduleId: Int
    ): Response<GetClinicScheduleDetailResponse>

    @POST("clinic/schedule")
    suspend fun createClinicScheduleList(
        @Body body: CreateClinicScheduleBody
    ): Response<CreateClinicScheduleResponse>

    @PUT("clinic/schedule/{id}")
    suspend fun updateClinicScheduleList(
        @Path("id") clinicScheduleId: Int,
        @Body body: UpdateClinicScheduleBody
    ): Response<UpdateClinicScheduleResponse>

    @DELETE("clinic/schedule/{id}")
    suspend fun deleteClinicScheduleList(
        @Path("id") clinicScheduleId: Int
    ): Response<DeleteClinicScheduleResponse>
}