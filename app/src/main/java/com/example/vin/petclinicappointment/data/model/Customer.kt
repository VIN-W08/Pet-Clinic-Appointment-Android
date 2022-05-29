package com.example.vin.petclinicappointment.data.model

class Customer(
    email: String,
    password: String,
    name: String? = null
): User(
    email = email,
    password = password,
    name = name
)