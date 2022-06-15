package com.example.vin.petclinicappointment.data.repository

import com.example.vin.petclinicappointment.data.model.*
import com.example.vin.petclinicappointment.data.network.ServiceApiService
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceRepository {

    private val serviceApiService = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:7284/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ServiceApiService::class.java)

    suspend fun getServiceList(clinicId: Int?, name: String?): Call<Response<GetServiceListResponse>> {
        val response = serviceApiService.getServiceList(clinicId, name)
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun getServiceDetail(id: Int): Call<Response<ServiceDetailResponse>>{
        val response = serviceApiService.getServiceDetail(id)
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun addService(body: CreateServiceBody): Call<Response<CreateServiceResponse>> {
        val response = serviceApiService.createService(body)
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun updateService(serviceId: Int, body: UpdateServiceBody): Call<Response<UpdateServiceResponse>>{
        val response = serviceApiService.updateService(serviceId, body)
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun deleteService(serviceId: Int): Call<Response<DeleteServiceResponse>>{
        val response = serviceApiService.deleteService(serviceId)
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }
}