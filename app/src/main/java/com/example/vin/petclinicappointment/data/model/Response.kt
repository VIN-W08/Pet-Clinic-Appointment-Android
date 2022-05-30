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
    val customer: CustomerData,
    val status: Boolean,
    val role: String,
    val created_at: String
)

data class CustomerData (
    val customer_id: Int,
    val name: String,
    val email: String,
    val password: String,
    val phone_num: String?,
    val updated_at: String?,
    val created_at: String
    )

data class PetClinicListResponse (
    val status: ResponseStatus,
    val data: List<PetClinic>
)
