package com.example.vin.petclinicappointment.ui.components.appointment

import androidx.lifecycle.viewModelScope
import com.example.vin.petclinicappointment.data.model.AppointmentDetail
import com.example.vin.petclinicappointment.data.model.Call
import com.example.vin.petclinicappointment.data.repository.AppointmentRepository
import com.example.vin.petclinicappointment.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AppointmentDetailViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository
): BaseViewModel() {
    private val _appointmentDetail = MutableStateFlow<AppointmentDetail?>(null)
    val appointmentDetail = _appointmentDetail.asStateFlow()

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
}