package com.example.vin.petclinicappointment.data.network

import com.example.vin.petclinicappointment.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface PetClinicApiService {
    @GET("petclinic/list")
    suspend fun getPetClinicList(
        @Query("name") name: String? = "",
        @Query("latitude") latitude: Double? = null,
        @Query("longitude") longitude: Double? = null
    ): Response<PetClinicListResponse>

    @GET("petclinic/{id}")
    suspend fun getPetClinicDetail(
        @Path("id") id: Int
    ): Response<PetClinicDetailResponse>

    @GET("service/{id}")
    suspend fun getServiceDetail(
        @Path("id") id: Int
    ): Response<ServiceDetailResponse>

    @GET("service/schedule/list")
    suspend fun getServiceScheduleList(
        @Query("service_id") serviceId: Int,
        @Query("start_schedule") startSchedule: String
    ): Response<ServiceScheduleListResponse>
}