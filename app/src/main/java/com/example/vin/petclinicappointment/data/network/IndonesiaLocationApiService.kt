package com.example.vin.petclinicappointment.data.network

import com.example.vin.petclinicappointment.data.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IndonesiaLocationApiService {
    @GET("kelurahan/{id}")
    suspend fun getVillageDetail(
       @Path("id") villageId: Long
    ): Response<Village>

    @GET("kecamatan/{id}")
    suspend fun getDistrictDetail(
        @Path("id") districtId: Long
    ): Response<District>

    @GET("kota/{id}")
    suspend fun getCityDetail(
        @Path("id") cityId: Long
    ): Response<City>

    @GET("provinsi/{id}")
    suspend fun getProvinceDetail(
        @Path("id") provinceId: Long
    ): Response<Province>

    @GET("kelurahan")
    suspend fun getVillageList(
        @Query("id_kecamatan") districtId: Long
    ): Response<GetVillageListResponse>

    @GET("kecamatan")
    suspend fun getDistrictList(
        @Query("id_kota") cityId: Long
    ): Response<GetDistrictListResponse>

    @GET("kota")
    suspend fun getCityList(
        @Query("id_provinsi") provinceId: Long
    ): Response<GetCityListResponse>

    @GET("provinsi")
    suspend fun getProvinceLIst(
    ): Response<GetProvinceListResponse>

}