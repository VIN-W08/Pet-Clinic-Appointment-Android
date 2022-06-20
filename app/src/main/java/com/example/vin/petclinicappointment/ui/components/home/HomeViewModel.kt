package com.example.vin.petclinicappointment.ui.components.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vin.petclinicappointment.data.model.AppointmentDetail
import com.example.vin.petclinicappointment.data.model.Call
import com.example.vin.petclinicappointment.data.repository.AppointmentRepository
import com.example.vin.petclinicappointment.data.repository.UserRepository
import com.example.vin.petclinicappointment.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    private val userRepository: UserRepository,
): BaseViewModel() {

    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()

    private val _todayNotFinishedAppointmentList = MutableStateFlow(listOf<AppointmentDetail>())
    val todayNotFinishedAppointmentList = _todayNotFinishedAppointmentList.asStateFlow()

    suspend fun getUserData() {
        viewModelScope.launch {
            _username.value = userRepository.getUserName() ?: ""
        }
    }

    suspend fun getTodayNotFinishedAppointmentList(){
        val userId = userRepository.getUserId()
        if(userId !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                Log.d("debug1", "start date:${LocalDate.now()}")
                appointmentRepository.getAppointmentList(
                    customerId = userId,
                    startSchedule = "${LocalDate.now()}T00:00:00",
                    finished = false
                )
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()?.data
                    if(data !== null) {
                        _todayNotFinishedAppointmentList.value = data
                    } else {
                        setMessage(response.data?.body()?.status?.message as String)
                    }
                }
                else -> setMessage(response.data?.message() as String)
            }
        }
    }
}