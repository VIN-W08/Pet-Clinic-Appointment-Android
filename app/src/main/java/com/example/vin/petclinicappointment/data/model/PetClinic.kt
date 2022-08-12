package com.example.vin.petclinicappointment.data.model

import com.google.gson.annotations.SerializedName

open class PetClinic(
    id: Int? = null,
    email: String,
    password: String? = null,
    name: String? = null,
//    phoneNum: String? = null,
//    image: String? = null,
    @SerializedName("phone_num")
    val phoneNum: String? = null,
    val image: String? = null,
    val address: String? = null,
    @SerializedName("village_id")
    val villageId: Long? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val status: Boolean = false,
    val distance: Double? = null,
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

class PetClinicDetail(
    id: Int,
    name: String,
    email: String,
    password: String,
    phoneNum: String,
    image: String,
    address: String,
    villageId: Long,
    latitude: Double,
    longitude: Double,
    status: Boolean,
    distance: Double,
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
    latitude,
    longitude,
    status,
    distance,
    updatedAt,
    createdAt
)

