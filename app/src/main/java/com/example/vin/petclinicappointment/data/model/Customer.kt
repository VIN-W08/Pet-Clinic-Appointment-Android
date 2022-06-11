package com.example.vin.petclinicappointment.data.model

class Customer(
    id: Int? = null,
    email: String,
    password: String? = null,
    name: String? = null,
    phoneNum: String? = null,
    image: String? = null,
    updatedAt: String? = null,
    createdAt: String? = null
): User(
    id = id,
    email = email,
    password = password,
    name = name,
    phoneNum = phoneNum,
    image = image,
    updatedAt = updatedAt,
    createdAt = createdAt
)