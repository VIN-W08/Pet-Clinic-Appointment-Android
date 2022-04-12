package com.example.vin.petclinicappointment.data.model

data class PetClinic(
    val id: String,
    val name: String,
    val image: Int? = null,
    val phone: String,
    val address: String,
    val rating: Double,
    val ratingCount: Int,
    val latitude: Double? = null,
    val longitude: Double? = null
)
