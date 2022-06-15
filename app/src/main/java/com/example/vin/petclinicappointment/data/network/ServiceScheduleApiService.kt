package com.example.vin.petclinicappointment.data.network

import com.example.vin.petclinicappointment.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ServiceScheduleApiService {
    @GET("service/schedule/list")
    suspend fun getServiceScheduleList(
        @Query("service_id") serviceId: Int,
        @Query("start_schedule") startSchedule: String
    ): Response<ServiceScheduleListResponse>

    @POST("service/schedule")
    suspend fun createServiceSchedule(
        @Body body: CreateServiceScheduleBody
    ): Response<CreateServiceScheduleResponse>

    @GET("service/schedule/{id}")
    suspend fun getServiceScheduleDetail(
        @Path("id") serviceScheduleId: Int
    ): Response<GetServiceScheduleDetailResponse>

    @PUT("service/schedule/{id}")
    suspend fun updateServiceSchedule(
        @Path("id") serviceScheduleId: Int,
        @Body body: UpdateServiceScheduleBody
    ): Response<UpdateServiceScheduleResponse>

    @DELETE("service/schedule/{id}")
    suspend fun deleteServiceSchedule(
        @Path("id") serviceScheduleId: Int
    ): Response<DeleteServiceScheduleResponse>
}