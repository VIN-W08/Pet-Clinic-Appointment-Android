package com.example.vin.petclinicappointment.ui.components.service

import androidx.lifecycle.viewModelScope
import com.example.vin.petclinicappointment.data.model.*
import com.example.vin.petclinicappointment.data.repository.ServiceRepository
import com.example.vin.petclinicappointment.data.repository.ServiceScheduleRepository
import com.example.vin.petclinicappointment.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ServiceDetailViewModel @Inject constructor(
   private val serviceRepository: ServiceRepository,
   private val serviceScheduleRepository: ServiceScheduleRepository
): BaseViewModel() {

    private val _serviceId = MutableStateFlow<Int?>(null)
    val serviceId = _serviceId.asStateFlow()

    private val _service = MutableStateFlow<Service?>(null)
    val service = _service.asStateFlow()

    private val _selectedServiceStartDate = MutableStateFlow<LocalDate>(LocalDate.now())
    val selectedServiceStartDate = _selectedServiceStartDate.asStateFlow()

    private val _serviceScheduleList = MutableStateFlow(listOf<ServiceSchedule>())
    val serviceScheduleList = _serviceScheduleList.asStateFlow()

    fun setServiceId(value: Int){
        _serviceId.value = value
    }

    fun setSelectedServiceStartDate(date: LocalDate){
        _selectedServiceStartDate.value = date
    }

    suspend fun getServiceDetail(){
        val serviceId = serviceId.value
        if(serviceId !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                serviceRepository.getServiceDetail(serviceId)
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()?.data
                    if (data !== null) {
                        _service.value = data
                    }else{
                        setMessage(response.data?.body()?.status?.message as String)
                    }
                }
                else -> {
                    setMessage(response.data?.message() as String)
                }
            }
        }
    }

    suspend fun getScheduleServiceList(){
        val serviceId = serviceId.value
        if(serviceId !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                serviceScheduleRepository.getServiceScheduleList(
                    serviceId,
                    "${selectedServiceStartDate.value}T00:00:00"
                )
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()?.data
                    if (data != null) {
                        _serviceScheduleList.value = data
                    }else{
                        setMessage(response.data?.body()?.status?.message as String)
                    }
                }
                else -> {
                    setMessage(response.data?.message() as String)
                }
            }
        }
    }
}