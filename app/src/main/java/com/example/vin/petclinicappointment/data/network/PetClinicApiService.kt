package com.example.vin.petclinicappointment.data.network

import com.example.vin.petclinicappointment.data.model.PetClinicListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PetClinicApiService {
    @GET("petclinic/list")
    suspend fun getPetClinicList(
        @Query("name") name: String? = "",
        @Query("latitude") latitude: Double? = null,
        @Query("longitude") longitude: Double? = null
    ): Response<PetClinicListResponse>
}