package com.example.vin.petclinicappointment.data.network

import com.example.vin.petclinicappointment.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("customer")
    suspend fun registerCustomer(
        @Body body: RegisterBody
    ): Response<RegisterResponse>

    @POST("customer/auth")
    suspend fun loginCustomer(
        @Body body: LoginBody
    ): Response<LoginResponse>
}