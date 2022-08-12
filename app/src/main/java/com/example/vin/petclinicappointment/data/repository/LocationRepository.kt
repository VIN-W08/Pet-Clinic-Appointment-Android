package com.example.vin.petclinicappointment.data.repository

import com.example.vin.petclinicappointment.data.model.*
import com.example.vin.petclinicappointment.data.network.GeocodeApiService
import com.example.vin.petclinicappointment.data.network.IndonesiaLocationApiService
import com.google.android.gms.maps.model.LatLng
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LocationRepository {
    private val geocodeApiService = Retrofit.Builder()
        .baseUrl("https://api.geoapify.com/v1/geocode/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GeocodeApiService::class.java)

    private val indonesiaLocationApiService = Retrofit.Builder()
        .baseUrl("https://dev.farizdotid.com/api/daerahindonesia/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(IndonesiaLocationApiService::class.java)

    suspend fun getReverseGeocodingLocation(coordinate: LatLng): GeocodingApiResult? {
        val response = geocodeApiService.getReverseGeocodingLocation(
            coordinate.latitude.toString(),
            coordinate.longitude.toString()
        )
        return if(response.isSuccessful){
            response.body()?.results?.first()
        } else {
            null
        }
    }

    suspend fun getAddressAutocompleteList(value: String): List<GeocodingApiResult>?{
        val response = geocodeApiService.getAddressAutocompleteList(value)
        return if(response.isSuccessful){
            response.body()?.results
        } else{
            null
        }
    }

    suspend fun getVillageDetail(villageId: Long): Call<Response<Village>> {
        val response = indonesiaLocationApiService.getVillageDetail(villageId)
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun getDistrictDetail(districtId: Long): Call<Response<District>> {
        val response = indonesiaLocationApiService.getDistrictDetail(districtId)
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun getCityDetail(cityId: Long): Call<Response<City>> {
        val response = indonesiaLocationApiService.getCityDetail(cityId)
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun getProvinceDetail(provinceId: Long): Call<Response<Province>> {
        val response = indonesiaLocationApiService.getProvinceDetail(provinceId)
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun getVillageList(districtId: Long): Call<Response<GetVillageListResponse>>{
        val response = indonesiaLocationApiService.getVillageList(districtId)
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun getDistrictList(cityId: Long): Call<Response<GetDistrictListResponse>>{
        val response = indonesiaLocationApiService.getDistrictList(cityId)
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun getCityList(provinceId: Long): Call<Response<GetCityListResponse>>{
        val response = indonesiaLocationApiService.getCityList(provinceId)
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }

    suspend fun getProvinceList(): Call<Response<GetProvinceListResponse>>{
        val response = indonesiaLocationApiService.getProvinceLIst()
        if(response.isSuccessful){
            return Call.Success(response)
        }
        return Call.Error(response)
    }
}