package com.example.vin.petclinicappointment.data.model

import com.google.gson.annotations.SerializedName

data class Village(
    val id: Long,
    @SerializedName("id_kecamatan")
    val districtId: String,
    @SerializedName("nama")
    val name: String
)

data class District(
    val id: Long,
    @SerializedName("id_kota")
    val cityId: String,
    @SerializedName("nama")
    val name: String
)

data class City(
    val id: Long,
    @SerializedName("id_provinsi")
    val provinceId: String,
    @SerializedName("nama")
    val name: String
)

data class Province(
    val id: Long,
    @SerializedName("nama")
    val name: String
)