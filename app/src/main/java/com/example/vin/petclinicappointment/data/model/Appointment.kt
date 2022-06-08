package com.example.vin.petclinicappointment.data.model

import com.google.gson.annotations.SerializedName

open class Appointment (
        @SerializedName("appointment_id")
        val id: Int,
        @SerializedName("customer_id")
        val customerId: Int,
        @SerializedName("pet_clinic")
        val petClinic: PetClinic,
        @SerializedName("total_payable")
        val totalPayable: Float,
        @SerializedName("payment_status")
        val paymentStatus: Boolean,
        val note: String,
        val status: Int,
        @SerializedName("created_at")
        val createdAt: String
        )

class AppointmentDetail(
        id: Int,
        totalPayable: Float,
        paymentStatus: Boolean,
        note: String,
        status: Int,
        customerId: Int,
        customer: Customer,
        petClinic: PetClinic,
        val service: Service,
        @SerializedName("schedule_service")
        val serviceSchedule: ServiceSchedule,
        createdAt: String
): Appointment(
        id = id,
        customerId = customerId,
        petClinic = petClinic,
        totalPayable = totalPayable,
        paymentStatus = paymentStatus,
        note = note,
        status = status,
        createdAt = createdAt
)