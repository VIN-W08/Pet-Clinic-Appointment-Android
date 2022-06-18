package com.example.vin.petclinicappointment.data.model

import com.google.gson.annotations.SerializedName

abstract class User(
    @SerializedName("user_id")
    val id: Int? = null,
    val email: String,
    val password: String? = null,
    val name: String? = null,
    @SerializedName("phone_num")
    val phoneNum: String? = null,
    val image: String? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = null
)
