package com.example.vin.petclinicappointment.data.model

data class RegisterBody (
        val name: String,
        val email: String,
        val password: String
        )

data class LoginBody (
    val email: String,
    val password: String
)