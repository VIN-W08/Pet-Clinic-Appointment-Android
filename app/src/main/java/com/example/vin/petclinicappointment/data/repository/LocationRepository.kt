package com.example.vin.petclinicappointment.data.repository

import com.example.vin.petclinicappointment.data.model.GeocodingApiResult
import com.example.vin.petclinicappointment.data.network.ApiService
import com.google.android.gms.maps.model.LatLng
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LocationRepository {
    val apiService = Retrofit.Builder()
        .baseUrl("https://api.geoapify.com/v1/geocode/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    suspend fun getReverseGeocodingLocation(coordinate: LatLng): GeocodingApiResult? {
        val response = apiService.getReverseGeocodingLocation(
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
        val response = apiService.getAddressAutocompleteList(value)
        return if(response.isSuccessful){
            response.body()?.results
        } else{
            null
        }
    }
}