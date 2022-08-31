package com.example.vin.petclinicappointment.data.network

import com.example.vin.petclinicappointment.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface CustomerApiService {
    @POST("customer")
    suspend fun registerCustomer(
        @Body body: RegisterBody
    ): Response<RegisterCustomerResponse>

    @POST("customer/auth")
    suspend fun loginCustomer(
        @Body body: LoginBody
    ): Response<LoginCustomerResponse>

    @PUT("customer/{id}")
    suspend fun updateCustomer(
        @Path("id") customerId: Int,
        @Body body: UpdateCustomerBody
    ): Response<UpdateCustomerResponse>

    @PATCH("customer/password")
    suspend fun updatePassword(
        @Body body: UpdatePasswordBody
    ): Response<UpdatePasswordResponse>

    @GET("customer/{id}")
    suspend fun getCustomerDetail(
       @Path("id") customerId: Int
    ): Response<GetCustomerDetailResponse>
}