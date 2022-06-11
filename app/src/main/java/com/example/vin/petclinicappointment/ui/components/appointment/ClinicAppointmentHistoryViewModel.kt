package com.example.vin.petclinicappointment.ui.components.appointment

import androidx.lifecycle.viewModelScope
import com.example.vin.petclinicappointment.data.model.Appointment
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
class ClinicAppointmentHistoryViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    private val userRepository: UserRepository
): BaseViewModel() {
    private val _clinicFinishedAppointmentList = MutableStateFlow<List<Appointment>>(listOf())
    val clinicFinishedAppointmentList = _clinicFinishedAppointmentList.asStateFlow()

    suspend fun getClinicFinishedAppointmentList(){
        val userId = userRepository.getUserId()
        if(userId !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                appointmentRepository.getAppointmentList(
                    clinicId = userId,
                    finished = true
                )
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()?.data
                    if(data !== null) {
                        _clinicFinishedAppointmentList.value = data
                    }
                }
                else -> {
                    setMessage(response.data?.message() as String)
                }
            }
        }
    }
}