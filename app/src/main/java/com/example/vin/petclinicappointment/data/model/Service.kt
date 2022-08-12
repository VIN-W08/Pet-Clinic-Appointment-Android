package com.example.vin.petclinicappointment.data.model

import com.google.gson.annotations.SerializedName

open class Service(
    @SerializedName("service_id")
    val id: Int,
    val name: String,
    var price: Float,
    val status: Boolean
)

data class ServiceSchedule(
    @SerializedName("schedule_service_id")
    val id: Int,
    @SerializedName("start_schedule")
    val startSchedule: String,
    @SerializedName("end_schedule")
    val endSchedule: String,
    val quota: Int,
    val status: Boolean,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("created_at")
    val createdAt: String
)