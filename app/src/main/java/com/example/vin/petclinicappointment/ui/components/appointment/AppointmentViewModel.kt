package com.example.vin.petclinicappointment.ui.components.appointment

import androidx.lifecycle.viewModelScope
import com.example.vin.petclinicappointment.data.model.Appointment
import com.example.vin.petclinicappointment.data.model.AppointmentDetail
import com.example.vin.petclinicappointment.data.model.Call
import com.example.vin.petclinicappointment.data.repository.AppointmentRepository
import com.example.vin.petclinicappointment.data.repository.UserRepository
import com.example.vin.petclinicappointment.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    private val userRepository: UserRepository
): BaseViewModel() {

    private val _clinicUnfinishedAppointmentList = MutableStateFlow<List<Appointment>>(listOf())
    val clinicUnfinishedAppointmentList = _clinicUnfinishedAppointmentList.asStateFlow()

    private val _clinicRequestingAppointmentList = MutableStateFlow<List<Appointment>>(listOf())
    val clinicRequestingAppointmentList = _clinicRequestingAppointmentList.asStateFlow()

    private val _clinicApprovedAppointmentList = MutableStateFlow<List<AppointmentDetail>>(listOf())
    val clinicApprovedAppointmentList = _clinicApprovedAppointmentList.asStateFlow()

    suspend fun getClinicUnfinishedAppointmentList(){
        val userId = userRepository.getUserId()
        if(userId !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                appointmentRepository.getAppointmentList(
                    clinicId = userId,
                    finished = false
                )
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()?.data
                    if(data !== null) {
                        _clinicUnfinishedAppointmentList.value = data
                    } else {
                        setMessage(response.data?.body()?.status?.message as String)
                    }
                }
                else -> setMessage(response.data?.message() as String)
            }
        }
    }

    suspend fun getClinicRequestingAppointmentList(){
        val userId = userRepository.getUserId()
        if(userId !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                appointmentRepository.getAppointmentList(
                    clinicId = userId,
                    status = 0
                )
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()?.data
                    if(data !== null) {
                        _clinicRequestingAppointmentList.value = data
                    } else {
                        setMessage(response.data?.body()?.status?.message as String)
                    }
                }
                else -> setMessage(response.data?.message() as String)
            }
        }
    }

    suspend fun getClinicApprovedAppointmentList(){
        val userId = userRepository.getUserId()
        if(userId !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                appointmentRepository.getAppointmentList(
                    clinicId = userId,
                    status = 1
                )
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()?.data
                    if(data !== null) {
                        _clinicApprovedAppointmentList.value = data
                    } else {
                        setMessage(response.data?.body()?.status?.message as String)
                    }
                }
                else -> setMessage(response.data?.message() as String)
            }
        }
    }
}