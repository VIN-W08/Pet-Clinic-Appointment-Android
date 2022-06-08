package com.example.vin.petclinicappointment.ui.components.history

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
class HistoryViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    private val userRepository: UserRepository
): BaseViewModel() {
    private val _finishedAppointmentList = MutableStateFlow<List<Appointment>>(listOf())
    val finishedAppointmentList = _finishedAppointmentList.asStateFlow()

    private val _unfinishedAppointmentList = MutableStateFlow<List<Appointment>>(listOf())
    val unfinishedAppointmentList = _unfinishedAppointmentList.asStateFlow()

    suspend fun getFinishedAppointmentList(){
        val userId = userRepository.getUserId()
        if(userId !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                appointmentRepository.getAppointmentList(
                    customerId = userId,
                    finished = true
                )
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()?.data
                    if(data !== null) {
                        _finishedAppointmentList.value = data
                    }
                }
                else -> {
                    setMessage(response.data?.message() as String)
                }
            }
        }
    }

    suspend fun getUnfinishedAppointmentList(){
        val userId = userRepository.getUserId()
        if(userId !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                appointmentRepository.getAppointmentList(
                    customerId = userId,
                    finished = false
                )
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()?.data
                    if(data !== null) {
                        _unfinishedAppointmentList.value = data
                    }
                }
                else -> {
                    setMessage(response.data?.message() as String)
                }
            }
        }
    }
}