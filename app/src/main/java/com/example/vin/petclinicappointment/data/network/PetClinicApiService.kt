package com.example.vin.petclinicappointment.data.network

import com.example.vin.petclinicappointment.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface PetClinicApiService {
    @GET("petclinic/list")
    suspend fun getPetClinicList(
        @Query("name") name: String? = "",
        @Query("latitude") latitude: Double? = null,
        @Query("longitude") longitude: Double? = null,
        @Query("page_number") pageNumber: Int? = null,
        @Query("page_size") pageSize: Int? = null
    ): Response<PetClinicListResponse>

    @GET("petclinic/{id}")
    suspend fun getPetClinicDetail(
        @Path("id") id: Int
    ): Response<PetClinicDetailResponse>

    @Multipart
    @POST("petclinic")
    suspend fun registerPetClinic(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("phone_number") phoneNum: RequestBody,
        @Part("address") address: RequestBody,
        @Part("village_id") villageId: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody
    ): Response<RegisterClinicResponse>

    @Multipart
    @PUT("petclinic/{id}")
    suspend fun updatePetClinic(
        @Path("id") clinicId: Int? = null,
        @Part("name") name: RequestBody? = null,
        @Part("email") email: RequestBody? = null,
        @Part("phone_number") phoneNum: RequestBody? =null,
        @Part("address") address: RequestBody? = null,
        @Part("village_id") villageId: RequestBody? = null,
        @Part("latitude") latitude: RequestBody? = null,
        @Part("longitude") longitude: RequestBody?=null,
        @Part image: MultipartBody.Part? = null
    ): Response<UpdateClinicResponse>

    @PATCH("petclinic/{id}")
    suspend fun deletePetClinicImage(
       @Path("id") clinicId: Int
    ): Response<DeletePetClinicImageResponse>

    @POST("petclinic/auth")
    suspend fun loginPetClinic(
        @Body body: LoginBody
    ): Response<LoginClinicResponse>

    @PATCH("petclinic/password")
    suspend fun updatePassword(
        @Body body: UpdatePasswordBody
    ): Response<UpdatePasswordResponse>

    @PATCH("petclinic/{id}/status")
    suspend fun updateStatus(
        @Path("id") clinicId: Int,
        @Body body: UpdateClinicStatusBody
    ): Response<UpdateClinicStatusResponse>
}