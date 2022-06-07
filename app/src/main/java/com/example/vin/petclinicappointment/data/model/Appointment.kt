package com.example.vin.petclinicappointment.data.model

import com.google.gson.annotations.SerializedName

data class Appointment (
        @SerializedName("pet_clinic_id")
        val id: Int,
        @SerializedName("total_payable")
        val totalPayable: Float,
        @SerializedName("payment_status")
        val paymentStatus: Boolean,
        val note: String,
        val status: Int
        )