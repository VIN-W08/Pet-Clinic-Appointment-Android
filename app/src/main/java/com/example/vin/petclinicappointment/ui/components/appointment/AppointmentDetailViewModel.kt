package com.example.vin.petclinicappointment.ui.components.appointment

import androidx.lifecycle.viewModelScope
import com.example.vin.petclinicappointment.data.model.AppointmentDetail
import com.example.vin.petclinicappointment.data.model.Call
import com.example.vin.petclinicappointment.data.model.UpdateAppointmentStatusBody
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
class AppointmentDetailViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    private val userRepository: UserRepository
): BaseViewModel() {
    private val _appointmentDetail = MutableStateFlow<AppointmentDetail?>(null)
    val appointmentDetail = _appointmentDetail.asStateFlow()

    private val _userRole = MutableStateFlow<String?>(null)
    val userRole = _userRole.asStateFlow()

    private val _appointmentActionList = MutableStateFlow(listOf<Int>())
    val appointmentActionList = _appointmentActionList.asStateFlow()

    private val _selectedAppointmentStatusId = MutableStateFlow<Int?>(null)
    val selectedAppointmentStatusId = _selectedAppointmentStatusId.asStateFlow()

    val appointmentCodeToTextStatusMap = mapOf(
        0 to "requesting",
        1 to "approved",
        2 to "disapproved",
        3 to "canceled",
        4 to "completed"
    )

    fun setSelectedAppointmentStatusId(value: Int){
        _selectedAppointmentStatusId.value = value
    }

    suspend fun getAppointmentDetail(appointmentId: Int){
        val response = viewModelScope.async(Dispatchers.IO) {
            appointmentRepository.getAppointmentDetail(appointmentId)
        }.await()
        when (response) {
            is Call.Success -> {
                val data = response.data?.body()?.data
                if(data !== null) {
                    _appointmentDetail.value = data
                }
            }
            else -> {
                setMessage(response.data?.message() as String)
            }
        }
    }

    fun getAppointmentActionList(){
        val currentStatusCode = appointmentDetail.value?.status
        when(userRole.value) {
            "customer" -> {
                if(currentStatusCode == 0  || currentStatusCode == 1){
                    _appointmentActionList.value = listOf(3)
                }
            }
            "clinic" -> {
                if(currentStatusCode == 0){
                    _appointmentActionList.value = listOf(2, 1)
                }else if(currentStatusCode == 1){
                    _appointmentActionList.value = listOf(3, 4)
                }
            }
            else -> throw IllegalArgumentException()
        }
    }

    suspend fun updateAppointmentStatus(){
        val appointmentId = appointmentDetail.value?.id
        val selectedAppointmentStatusId = selectedAppointmentStatusId.value
        if(appointmentId !== null && selectedAppointmentStatusId!==null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                appointmentRepository.updateAppointmentStatus(
                    appointmentId,
                    UpdateAppointmentStatusBody(status = selectedAppointmentStatusId)
                )
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()?.data
                    if (data !== null) {
                        _appointmentDetail.value = data
                    }
                }
                else -> {
                    setMessage(response.data?.message() as String)
                }
            }
        }
    }

    suspend fun getUserRole(){
        val userRole = userRepository.getUserRole()
        if(!userRole.isNullOrEmpty()) {
            _userRole.value = userRole
        }
    }
}