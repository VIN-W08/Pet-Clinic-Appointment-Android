package com.example.vin.petclinicappointment.data.model

import com.google.gson.annotations.SerializedName

data class ClinicSchedule (
    @SerializedName("schedule_pet_clinic_id")
    val id: Int,
    val day: Int,
    val shift: Int,
    @SerializedName("start_time")
    val startTime: String,
    @SerializedName("end_time")
    val endTime: String
    )