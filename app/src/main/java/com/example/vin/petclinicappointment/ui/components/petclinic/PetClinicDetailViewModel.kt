package com.example.vin.petclinicappointment.ui.components.petclinic

import androidx.lifecycle.viewModelScope
import com.example.vin.petclinicappointment.data.model.*
import com.example.vin.petclinicappointment.data.repository.*
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
    private val serviceRepository: ServiceRepository,
    private val appointmentRepository: AppointmentRepository,
    private val serviceScheduleRepository: ServiceScheduleRepository,
    private val userRepository: UserRepository
): BaseViewModel() {

    private val _clinicDetail = MutableStateFlow<PetClinicDetail?>(null)
    val clinicDetail = _clinicDetail.asStateFlow()

    private val _service = MutableStateFlow<Service?>(null)
    val service = _service.asStateFlow()

    private val _selectedServiceId = MutableStateFlow<Int?>(null)
    val selectedServiceId = _selectedServiceId.asStateFlow()

    private val _selectedDate = MutableStateFlow<LocalDate>(LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

    private val _scheduleList = MutableStateFlow<List<ServiceSchedule>>(listOf())
    val scheduleList = _scheduleList.asStateFlow()

    private val _selectedScheduleId = MutableStateFlow<Int?>(null)
    val selectedScheduleId = _selectedScheduleId.asStateFlow()

    private val _selectedServiceName = MutableStateFlow("")
    val selectedServiceName = _selectedServiceName.asStateFlow()

    fun setSelectedScheduleId(id: Int?){
        _selectedScheduleId.value = id
    }

    fun setSelectedDate(date: LocalDate){
        _selectedDate.value = date
    }

    fun setSelectedServiceId(id: Int?){
        _selectedServiceId.value = id
    }

    fun setSelectedServiceName(value: String){
        _selectedServiceName.value = value
    }

    suspend fun getPetClinicDetail(id: Int){
        val response = viewModelScope.async(Dispatchers.IO) {
            petClinicRepository.getPetClinicDetail(id)
        }.await()
        when(response){
            is Call.Success -> {
                val data = response.data?.body()?.data
                if(data!==null) {
                    _clinicDetail.value = data
                }else{
                    setMessage(response.data?.body()?.status?.message as String)
                }
            }
            else -> setMessage(response.data?.message() as String)
        }
    }

    suspend fun getServiceDetail(id: Int){
        val response = viewModelScope.async(Dispatchers.IO) {
            serviceRepository.getServiceDetail(id)
        }.await()
        when(response){
            is Call.Success -> {
                val data = response.data?.body()?.data
                if(data !== null) {
                    _service.value = data
                }else{
                    setMessage(response.data?.body()?.status?.message as String)
                }
            }
            else -> { setMessage(response.data?.message() as String) }
        }
    }

    suspend fun getServiceScheduleList(){
        val selectedServiceId = selectedServiceId.value
        val selectedDate = selectedDate.value
        if(selectedServiceId !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                serviceScheduleRepository.getServiceScheduleList(
                    selectedServiceId,
                    "${selectedDate}T00:00:00"
                )
            }.await()
            when(response){
                is Call.Success -> {
                    val data = response.data?.body()?.data
                    if (data != null) {
                        _scheduleList.value = data
                    }else{
                        setMessage(response.data?.body()?.status?.message as String)
                    }
                }
                else -> { setMessage(response.data?.message() as String) }
            }
        }
    }

    suspend fun createAppointment(): Boolean {
        val selectedClinicId = clinicDetail.value?.id
        val selectedService = clinicDetail.value?.serviceList?.find { service -> service.id == selectedServiceId.value }
        val selectedSchedule = scheduleList.value.find { schedule ->  schedule.id == selectedScheduleId.value}
        val userId = userRepository.getUserId()
        if( userId !== null &&
            selectedClinicId !== null &&
            selectedService !== null &&
                    selectedSchedule !== null
        ) {
            val response = viewModelScope.async(Dispatchers.IO) {
                appointmentRepository.createAppointment(CreateAppointmentBody(
                    customerId = userId,
                    petClinicId = selectedClinicId,
                    serviceId = selectedService.id,
                    scheduleServiceId = selectedSchedule.id,
                    serviceName = selectedService.name,
                    servicePrice = selectedService.price,
                    startSchedule = selectedSchedule.startSchedule,
                    endSchedule = selectedSchedule.endSchedule
                ))
            }.await()
            return when (response) {
                is Call.Success -> {
                    val data = response.data?.body()?.data
                    if (data == null) {
                        setMessage(response.data?.body()?.status?.message as String)
                    }
                    data !== null
                }
                else -> {
                    setMessage(response.data?.message() as String)
                    return false
                }
            }
        }
        return false
    }
}
