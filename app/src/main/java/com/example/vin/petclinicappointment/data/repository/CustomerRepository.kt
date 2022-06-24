package com.example.vin.petclinicappointment.data.repository

import com.example.vin.petclinicappointment.data.model.*
import com.example.vin.petclinicappointment.data.network.CustomerApiService
import com.example.vin.petclinicappointment.data.network.PetClinicApiService
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CustomerRepository {
    private val customerApiService = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:7284/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CustomerApiService::class.java)

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
}