package com.example.vin.petclinicappointment.data.model

data class ResponseStatus (
    val code: Int,
    val message: String
    )

data class LoginResponse(
    val status: ResponseStatus,
    val data: AuthData?
)

data class RegisterResponse(
    val status: ResponseStatus,
    val data: AuthData?
)

data class AuthData(
    val customer: Customer,
    val status: Boolean,
    val role: String,
    val created_at: String
)

data class PetClinicListResponse (
    val status: ResponseStatus,
    val data: List<PetClinic>
)

data class PetClinicDetailResponse (
    val status: ResponseStatus,
    val data: PetClinicDetail
)

data class ServiceDetailResponse (
    val status: ResponseStatus,
    val data: ServiceDetail
    )

data class ServiceScheduleListResponse (
    val status: ResponseStatus,
    val data: List<ServiceSchedule>
        )

data class CreateAppointmentResponse(
    val status: ResponseStatus,
    val data: Appointment
)

data class GetAppointmentListResponse(
    val status: ResponseStatus,
    val data: List<Appointment>
)

data class GetAppointmentDetailResponse(
    val status: ResponseStatus,
    val data: AppointmentDetail
)

