package com.example.vin.petclinicappointment.data.network

import com.example.vin.petclinicappointment.BuildConfig
import com.example.vin.petclinicappointment.data.model.GeocodingApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodeApiService {
    @GET("autocomplete")
    suspend fun getAddressAutocompleteList(
        @Query("text") text: String,
        @Query("type") type: String? = null,
        @Query("lang") lang: String? = null,
        @Query("filter") filter: String? = null,
        @Query("format") format: String? = "json",
        @Query("apiKey") apiKey: String = BuildConfig.GEOAPIFY_API_KEY
    ): Response<GeocodingApiResponse>

    @GET("reverse")
    suspend fun getReverseGeocodingLocation(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("limit") limit: String = "1",
        @Query("type") type: String? = null,
        @Query("lang") lang: String? = null,
        @Query("format") format: String? = "json",
        @Query("apiKey") apiKey: String = BuildConfig.GEOAPIFY_API_KEY
    ): Response<GeocodingApiResponse>
}