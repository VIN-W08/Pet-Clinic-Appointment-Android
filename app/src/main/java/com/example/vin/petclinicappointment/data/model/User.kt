package com.example.vin.petclinicappointment.data.model

data class User(
    val email: String,
    val password: String,
    val id: String? = null,
    val name: String? = null,
    val address: String? = null
)
