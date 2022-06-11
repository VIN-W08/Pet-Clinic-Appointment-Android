package com.example.vin.petclinicappointment.data.network

import com.example.vin.petclinicappointment.data.model.*
import okhttp3.RequestBody
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

    @Multipart
    @POST("petclinic")
    suspend fun createPetClinic(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("phone_num") phoneNum: RequestBody,
        @Part("address") address: RequestBody,
        @Part("village_id") villageId: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody
    ): Response<RegisterClinicResponse>

    @POST("petclinic/auth")
    suspend fun loginPetClinic(
        @Body body: LoginBody
    ): Response<LoginClinicResponse>
}