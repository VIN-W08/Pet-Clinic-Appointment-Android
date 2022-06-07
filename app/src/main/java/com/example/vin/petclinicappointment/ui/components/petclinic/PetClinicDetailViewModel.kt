package com.example.vin.petclinicappointment.ui.components.petclinic

import androidx.lifecycle.viewModelScope
import com.example.vin.petclinicappointment.data.model.*
import com.example.vin.petclinicappointment.data.repository.AppointmentRepository
import com.example.vin.petclinicappointment.data.repository.PetClinicRepository
import com.example.vin.petclinicappointment.data.repository.UserRepository
import com.example.vin.petclinicappointment.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class PetClinicDetailViewModel @Inject constructor(
    private val petClinicRepository: PetClinicRepository,
    private val appointmentRepository: AppointmentRepository,
    private val userRepository: UserRepository
): BaseViewModel() {
    private val _clinicDetail = MutableStateFlow<PetClinicDetail?>(null)
    val clinicDetail = _clinicDetail.asStateFlow()

    private val _serviceDetail = MutableStateFlow<ServiceDetail?>(null)
    val serviceDetail = _serviceDetail.asStateFlow()

    private val _selectedServiceId = MutableStateFlow<Int?>(null)
    val selectedServiceId = _selectedServiceId.asStateFlow()

    private val _selectedDate = MutableStateFlow<LocalDate>(LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

    private val _scheduleList = MutableStateFlow<List<ServiceSchedule>>(listOf())
    val scheduleList = _scheduleList.asStateFlow()

    private val _selectedScheduleId = MutableStateFlow<Int?>(null)
    val selectedScheduleId = _selectedScheduleId.asStateFlow()

    private val _appointment = MutableStateFlow<Appointment?>(null)
    val appointment = _appointment.asStateFlow()

    fun setSelectedScheduleId(id: Int?){
        _selectedScheduleId.value = id
    }

    fun setSelectedDate(date: LocalDate){
        _selectedDate.value = date
    }

    fun setSelectedServiceId(id: Int?){
        _selectedServiceId.value = id
    }

    suspend fun getPetClinicDetail(id: Int){
        val response = viewModelScope.async(Dispatchers.IO) {
            petClinicRepository.getPetClinicDetail(id)
        }.await()
        when(response){
            is Call.Success -> {
                val data = response.data?.body()?.data
                _clinicDetail.value = data
            }
            else -> setMessage(response.data?.message() as String)
        }
    }

    suspend fun getServiceDetail(id: Int){
        val response = viewModelScope.async(Dispatchers.IO) {
            petClinicRepository.getServiceDetail(id)
        }.await()
        when(response){
            is Call.Success -> {
                val data = response.data?.body()?.data
                _serviceDetail.value = data
            }
            else -> { setMessage(response.data?.message() as String) }
        }
    }

    suspend fun getServiceScheduleList(){
        val selectedServiceId = selectedServiceId.value
        val selectedDate = selectedDate.value
        if(selectedServiceId !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                petClinicRepository.getServiceScheduleList(
                    selectedServiceId,
                    "${selectedDate}T00:00:00"
                )
            }.await()
            when(response){
                is Call.Success -> {
                    val data = response.data?.body()?.data
                    if (data != null) {
                        _scheduleList.value = data
                    }
                }
                else -> { setMessage(response.data?.message() as String) }
            }
        }
    }

    suspend fun createAppointment(){
        val selectedServiceId = selectedServiceId.value
        val selectedServiceDetail = serviceDetail.value
        val selectedScheduleId = selectedScheduleId.value
        val userId = userRepository.getUserId()
        if( userId !== null &&
            selectedServiceId !== null &&
            selectedServiceDetail !== null &&
            selectedScheduleId !== null
        ) {
            val response = viewModelScope.async(Dispatchers.IO) {
                appointmentRepository.createAppointment(CreateAppointmentBody(
                    customerId = userId,
                    petClinicId = selectedServiceDetail.petClinicId,
                    serviceId = selectedServiceId,
                    scheduleServiceId = selectedScheduleId,
                    totalPayable = selectedServiceDetail.price
                ))
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()?.data
                    if (data != null) {
                        _appointment.value = data
                    }
                }
                else -> setMessage(response.data?.message() as String)
            }
        }
    }
}
