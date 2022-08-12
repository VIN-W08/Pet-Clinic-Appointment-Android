package com.example.vin.petclinicappointment.data.model

class Customer(
    id: Int? = null,
    email: String,
    password: String? = null,
    name: String? = null,
    updatedAt: String? = null,
    createdAt: String? = null
): User(
    id = id,
    email = email,
    password = password,
    name = name,
    updatedAt = updatedAt,
    createdAt = createdAt
)