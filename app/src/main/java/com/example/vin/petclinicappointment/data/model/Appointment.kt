package com.example.vin.petclinicappointment.data.model

import com.google.gson.annotations.SerializedName

open class Appointment (
        @SerializedName("appointment_id")
        val id: Int,
        @SerializedName("customer")
        val customer: Customer,
        @SerializedName("pet_clinic")
        val petClinic: PetClinic,
        val note: String,
        val status: Int,
        @SerializedName("created_at")
        val createdAt: String
        )

class AppointmentDetail(
        id: Int,
        note: String,
        status: Int,
        customerId: Int,
        customer: Customer,
        petClinic: PetClinic,
        val service: Service,
        @SerializedName("schedule_service")
        val serviceSchedule: ServiceSchedule,
        @SerializedName("service_name")
        val serviceName: String,
        @SerializedName("service_price")
        val servicePrice: Float,
        @SerializedName("start_schedule")
        val startSchedule: String,
        @SerializedName("end_schedule")
        val endSchedule: String,
        createdAt: String
): Appointment(
        id = id,
        customer = customer,
        petClinic = petClinic,
        note = note,
        status = status,
        createdAt = createdAt
)