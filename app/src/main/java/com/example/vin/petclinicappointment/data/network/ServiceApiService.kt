package com.example.vin.petclinicappointment.data.network

import com.example.vin.petclinicappointment.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ServiceApiService {

    @GET("service/{id}")
    suspend fun getServiceDetail(
        @Path("id") id: Int
    ): Response<ServiceDetailResponse>

    @GET("service/list")
    suspend fun getServiceList(
        @Query("clinic_id") serviceId: Int?,
        @Query("name") startSchedule: String?
    ): Response<GetServiceListResponse>

    @POST("service")
    suspend fun createService(
        @Body body: CreateServiceBody
    ): Response<CreateServiceResponse>

    @PUT("service/{id}")
    suspend fun updateService(
        @Path("id") serviceId: Int,
        @Body body: UpdateServiceBody
    ): Response<UpdateServiceResponse>

    @DELETE("service/{id}")
    suspend fun deleteService(
        @Path("id") serviceId: Int
    ): Response<DeleteServiceResponse>
}