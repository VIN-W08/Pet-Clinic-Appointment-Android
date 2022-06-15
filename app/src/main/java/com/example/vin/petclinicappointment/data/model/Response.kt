package com.example.vin.petclinicappointment.data.model

import com.google.gson.annotations.SerializedName

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

data class RegisterClinicResponse(
    val status: ResponseStatus,
    val data: ClinicAuthData?
)

data class LoginClinicResponse(
    val status: ResponseStatus,
    val data: ClinicAuthData?
)

data class ClinicAuthData(
    val pet_clinic: PetClinic,
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

data class GetVillageListResponse (
    @SerializedName("kelurahan")
    val villageList: List<Village>
    )

data class GetDistrictListResponse (
    @SerializedName("kecamatan")
    val districtList: List<District>
)

data class GetCityListResponse (
    @SerializedName("kota_kabupaten")
    val cityList: List<City>
)

data class GetProvinceListResponse (
    @SerializedName("provinsi")
    val provinceList: List<Province>
)

data class UpdateAppointmentStatusResponse (
    val status: ResponseStatus,
    val data: AppointmentDetail
        )

data class GetServiceListResponse(
    val status: ResponseStatus,
    val data: List<Service>
)

data class CreateServiceResponse(
    val status: ResponseStatus,
    val data: Service
)

data class UpdateServiceResponse(
    val status: ResponseStatus,
    val data: Service
)

data class CreateServiceScheduleResponse(
    val status: ResponseStatus,
    val data: ServiceSchedule
)

data class GetServiceScheduleDetailResponse(
    val status: ResponseStatus,
    val data: ServiceSchedule
)

data class UpdateServiceScheduleResponse(
    val status: ResponseStatus,
    val data: ServiceSchedule
)

data class DeleteServiceResponse(
    val status: ResponseStatus,
    val data: Service
)

data class DeleteServiceScheduleResponse(
    val status: ResponseStatus,
    val data: ServiceSchedule
)
