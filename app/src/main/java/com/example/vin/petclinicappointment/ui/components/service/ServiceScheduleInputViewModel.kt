package com.example.vin.petclinicappointment.ui.components.service

import androidx.lifecycle.viewModelScope
import com.example.vin.petclinicappointment.data.model.*
import com.example.vin.petclinicappointment.data.repository.ServiceScheduleRepository
import com.example.vin.petclinicappointment.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ServiceScheduleInputViewModel @Inject constructor(
    private val serviceScheduleRepository: ServiceScheduleRepository
): BaseViewModel() {

    private val _serviceId = MutableStateFlow<Int?>(null)
    val serviceId = _serviceId.asStateFlow()

    private val _serviceScheduleId = MutableStateFlow<Int?>(null)
    val serviceScheduleId = _serviceScheduleId.asStateFlow()

    private val _serviceScheduleDetail = MutableStateFlow<ServiceSchedule?>(null)
    val serviceScheduleDetail = _serviceScheduleDetail.asStateFlow()

    private val _startDate = MutableStateFlow<LocalDate?>(null)
    val startDate = _startDate.asStateFlow()

    private val _endDate = MutableStateFlow<LocalDate?>(null)
    val endDate = _endDate.asStateFlow()

    private val _starTime = MutableStateFlow<LocalTime?>(null)
    val startTime = _starTime.asStateFlow()

    private val _endTime = MutableStateFlow<LocalTime?>(null)
    val endTime = _endTime.asStateFlow()

    private val _quota = MutableStateFlow("")
    val quota = _quota.asStateFlow()

    private val _status = MutableStateFlow(false)
    val status = _status.asStateFlow()

    fun setServiceId(value: Int){
        _serviceId.value = value
    }

    fun setServiceScheduleId(value: Int){
        _serviceScheduleId.value = value
    }

    fun setStartDate(value: LocalDate){
        _startDate.value = value
    }

    fun setEndDate(value: LocalDate){
        _endDate.value = value
    }

    fun setStartTime(value: LocalTime){
        _starTime.value = value
    }

    fun setEndTime(value: LocalTime){
        _endTime.value = value
    }

    fun setQuota(value: String){
        _quota.value = value
    }

    fun setStatus(value: Boolean){
        _status.value = value
    }

    fun populateServiceScheduleData(){
        val serviceScheduleDetail = serviceScheduleDetail.value
        if(serviceScheduleDetail !== null) {
            _startDate.value = LocalDateTime.parse(serviceScheduleDetail.startSchedule).toLocalDate()
            _starTime.value = LocalDateTime.parse(serviceScheduleDetail.startSchedule).toLocalTime()
            _endDate.value = LocalDateTime.parse(serviceScheduleDetail.endSchedule).toLocalDate()
            _endTime.value = LocalDateTime.parse(serviceScheduleDetail.endSchedule).toLocalTime()
            _quota.value = serviceScheduleDetail.quota.toString()
            _status.value = serviceScheduleDetail.status
        }
    }

    private fun validateServiceSchedule(): Boolean{
        if(startDate.value == null){
            setMessage("Tanggal mulai layanan wajib diinput")
            return false
        }
        if(startTime.value == null){
            setMessage("Waktu mulai layanan wajib diinput")
            return false
        }
        if(endDate.value == null){
            setMessage("Tanggal berakhir layanan wajib diinput")
            return false
        }
        if(endTime.value == null){
            setMessage("Waktu berakhir layanan wajib diinput")
            return false
        }
        if(quota.value.isEmpty()){
            setMessage("Kuota layanan wajib diinput")
            return false
        }
        return true
    }

    suspend fun getServiceScheduleDetail(){
        val serviceScheduleId = serviceScheduleId.value
        if(serviceScheduleId!==null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                serviceScheduleRepository.getServiceScheduleDetail(serviceScheduleId)
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()?.data
                    if(data !== null){
                        _serviceScheduleDetail.value = data
                    }
                }
                else -> {
                    setMessage(response.data?.message() as String)
                }
            }
        }
    }


    suspend fun updateServiceSchedule(): Boolean {
        if(validateServiceSchedule()) {
            val serviceScheduleId = serviceScheduleId.value
            if (serviceScheduleId !== null) {
                val response = viewModelScope.async(Dispatchers.IO) {
                    serviceScheduleRepository.updateServiceSchedule(
                        serviceScheduleId,
                        UpdateServiceScheduleBody(
                            startSchedule = LocalDateTime.of(startDate.value, startTime.value)
                                .format(
                                    DateTimeFormatter.ISO_LOCAL_DATE_TIME
                                ),
                            endSchedule = LocalDateTime.of(endDate.value, endTime.value).format(
                                DateTimeFormatter.ISO_LOCAL_DATE_TIME
                            ),
                            quota = quota.value.toInt(),
                            status = status.value
                        )
                    )
                }.await()
                return when (response) {
                    is Call.Success -> {
                        val data = response.data?.body()?.data
                        data !== null
                    }
                    else -> {
                        val errorJson = JSONObject((response.data as Response<ResponseStatus>).errorBody()?.string())
                        setMessage(errorJson.getJSONObject("status").getString("message"))
                        false
                    }
                }
            }
            return false
        }
        return false
    }

    suspend fun addServiceSchedule(): Boolean {
        if (validateServiceSchedule()) {
            val serviceId = serviceId.value
            if (serviceId !== null) {
                val response = viewModelScope.async(Dispatchers.IO) {
                    serviceScheduleRepository.createServiceSchedule(
                        CreateServiceScheduleBody(
                            serviceId = serviceId,
                            startSchedule = LocalDateTime.of(startDate.value, startTime.value)
                                .format(
                                    DateTimeFormatter.ISO_LOCAL_DATE_TIME
                                ),
                            endSchedule = LocalDateTime.of(endDate.value, endTime.value).format(
                                DateTimeFormatter.ISO_LOCAL_DATE_TIME
                            ),
                            quota = quota.value.toInt()
                        )
                    )
                }.await()
                return when (response) {
                    is Call.Success -> {
                        val data = response.data?.body()?.data
                        data !== null
                    }
                    else -> {
                        val errorJson = JSONObject((response.data as Response<ResponseStatus>).errorBody()?.string())
                        setMessage(errorJson.getJSONObject("status").getString("message"))
                        false
                    }
                }
            }
            return false
        }
        return false
    }

    suspend fun deleteServiceSchedule(): Boolean {
        val serviceScheduleId = serviceScheduleId.value
        if (serviceScheduleId !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                serviceScheduleRepository.deleteServiceSchedule(serviceScheduleId)
            }.await()
            return when (response) {
                is Call.Success -> {
                    val data = response.data?.body()?.data
                    data !== null
                }
                else -> {
                    val errorJson = JSONObject((response.data as Response<ResponseStatus>).errorBody()?.string())
                    setMessage(errorJson.getJSONObject("status").getString("message"))
                    false
                }
            }
        }
        return false
    }
}