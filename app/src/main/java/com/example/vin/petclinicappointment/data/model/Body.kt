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
    @SerializedName("service_name")
    val serviceName: String,
    @SerializedName("service_price")
    val servicePrice: Float,
    @SerializedName("start_schedule")
    val startSchedule: String,
    @SerializedName("end_schedule")
    val endSchedule: String
        )

data class UpdateAppointmentStatusBody(
    val status: Int
)

data class CreateServiceBody(
    @SerializedName("pet_clinic_id")
    val petClinicId: Int,
    val name: String,
    val price: Float
)

data class UpdateServiceBody(
    val name: String,
    val price: Float,
    val status: Boolean
)

data class CreateServiceScheduleBody(
    @SerializedName("service_id")
    val serviceId: Int,
    @SerializedName("start_schedule")
    val startSchedule: String,
    @SerializedName("end_schedule")
    val endSchedule: String,
    val quota: Int,
    @SerializedName("repeat_schedule_week_count")
    val repeatScheduleWeekCount: Int = 0
)

data class UpdateServiceScheduleBody(
    @SerializedName("start_schedule")
    val startSchedule: String,
    @SerializedName("end_schedule")
    val endSchedule: String,
    val quota: Int,
    val status: Boolean
)

data class CreateClinicScheduleBody(
    @SerializedName("pet_clinic_id")
    val petClinicId: Int,
    val day: Int,
    @SerializedName("start_time")
    val startTime: String,
    @SerializedName("end_time")
    val endTime: String
)

data class UpdateClinicScheduleBody(
    val day: Int?,
    val shift: Int?,
    @SerializedName("start_time")
    val startTime: String?,
    @SerializedName("end_time")
    val endTime: String?
)

data class UpdateCustomerBody(
    val name: String,
    val email: String
)

data class UpdatePasswordBody(
    val email: String,
    val password: String
)

data class UpdateClinicStatusBody(
    val status: Boolean
)