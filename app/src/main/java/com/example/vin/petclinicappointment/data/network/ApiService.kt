package com.example.vin.petclinicappointment.data.network

import com.example.vin.petclinicappointment.data.model.LoginBody
import com.example.vin.petclinicappointment.data.model.LoginResponse
import com.example.vin.petclinicappointment.data.model.RegisterBody
import com.example.vin.petclinicappointment.data.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST

interface ApiService {
    @POST("customer")
    suspend fun registerCustomer(
        @Body body: RegisterBody
    ): Response<RegisterResponse>

    @POST("auth")
    suspend fun loginCustomer(
        @Body body: LoginBody
    ): Response<LoginResponse>
}