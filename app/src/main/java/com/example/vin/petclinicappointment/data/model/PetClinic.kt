package com.example.vin.petclinicappointment.data.model

import retrofit2.http.Field

data class PetClinic (
    @Field("pet_clinic_id")
    val id: Int,
    val name: String,
    val email: String,
    @Field("phone_num")
    val phoneNum: String,
    val address: String,
    @Field("village_id")
    val villageId: Int,
    val rating: Int,
    val latitude: Double,
    val longitude: Double,
    @Field("updated_at")
    val updatedAt: String,
    @Field("created_at")
    val createdAt: String
        )

