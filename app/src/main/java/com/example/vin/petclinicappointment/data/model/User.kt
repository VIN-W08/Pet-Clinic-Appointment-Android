package com.example.vin.petclinicappointment.data.model

abstract class User(
    val email: String,
    val password: String,
    val id: String? = null,
    val name: String? = null,
    val phoneNum: String? = null,
    val image: String? = null
)
