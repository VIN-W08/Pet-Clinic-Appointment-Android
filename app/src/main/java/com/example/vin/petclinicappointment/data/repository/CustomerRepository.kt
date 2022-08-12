package com.example.vin.petclinicappointment.data.repository

import com.example.vin.petclinicappointment.data.model.*
import com.example.vin.petclinicappointment.data.network.CustomerApiService
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CustomerRepository {
    private val customerApiService = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:7284/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CustomerApiService::class.java)

    suspend fun loginCustomer(body: LoginBody): Call<Response<LoginResponse>> {
        val response = customerApiService.loginCustomer(body)
        if(response.isSuccessful) {
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun registerCustomer(body: RegisterBody): Call<Response<RegisterResponse>>{
        val response = customerApiService.registerCustomer(body)
        if(response.isSuccessful) {
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun updateCustomer(id: Int, body: UpdateCustomerBody): Call<Response<UpdateCustomerResponse>> {
        val response = customerApiService.updateCustomer(id, body)
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun updatePassword(body: UpdatePasswordBody): Call<Response<UpdatePasswordResponse>>{
        val response = customerApiService.updatePassword(body)
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun getCustomerDetail(id: Int): Call<Response<GetCustomerDetailResponse>>{
        val response = customerApiService.getCustomerDetail(id)
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }
}