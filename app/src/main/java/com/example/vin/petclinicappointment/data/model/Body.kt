package com.example.vin.petclinicappointment.data.model

import com.google.gson.annotations.SerializedName

data class RegisterBody (
        val name: String,
        val email: String,
        val password: String
        )

data class LoginBody (
    val email: String,
    val password: String
)

data class CreateAppointmentBody (
    @SerializedName("customer_id")
    val customerId: Int,
    @SerializedName("pet_clinic_id")
    val petClinicId: Int,
    @SerializedName("service_id")
    val serviceId: Int,
    @SerializedName("schedule_service_id")
    val scheduleServiceId: Int,
    @SerializedName("total_payable")
    val totalPayable: Float
        )
