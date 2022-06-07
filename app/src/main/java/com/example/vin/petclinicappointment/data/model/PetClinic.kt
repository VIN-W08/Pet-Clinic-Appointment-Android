package com.example.vin.petclinicappointment.data.model

import com.google.gson.annotations.SerializedName

open class PetClinic(
    id: Int? = null,
    email: String,
    password: String,
    name: String? = null,
    phoneNum: String? = null,
    image: String? = null,
    val address: String,
    @SerializedName("village_id")
    val villageId: Long,
    val rating: Int,
    val latitude: Double,
    val longitude: Double,
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

class PetClinicDetail(
    id: Int,
    name: String,
    email: String,
    password: String,
    phoneNum: String,
    image: String,
    address: String,
    villageId: Long,
    rating: Int,
    latitude: Double,
    longitude: Double,
    @SerializedName("services")
    val serviceList: List<Service>,
    @SerializedName("schedules_pet_clinic")
    val clinicScheduleList: List<ClinicSchedule>,
    updatedAt: String,
    createdAt: String,
): PetClinic(
    id,
    email,
    password,
    name,
    phoneNum,
    image,
    address,
    villageId,
    rating,
    latitude,
    longitude,
    updatedAt,
    createdAt
)

