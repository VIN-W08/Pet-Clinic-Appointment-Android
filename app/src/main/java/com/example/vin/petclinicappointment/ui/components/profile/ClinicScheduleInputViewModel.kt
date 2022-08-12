package com.example.vin.petclinicappointment.ui.components.profile

import androidx.lifecycle.viewModelScope
import com.example.vin.petclinicappointment.data.model.*
import com.example.vin.petclinicappointment.data.repository.ClinicScheduleRepository
import com.example.vin.petclinicappointment.data.repository.UserRepository
import com.example.vin.petclinicappointment.ui.BaseViewModel
import com.example.vin.petclinicappointment.ui.components.common.DropdownOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ClinicScheduleInputViewModel @Inject constructor(
    private val clinicScheduleRepository: ClinicScheduleRepository,
    private val userRepository: UserRepository
): BaseViewModel() {

    private val _day = MutableStateFlow<DropdownOption?>(null)
    val day = _day.asStateFlow()

    private val _startTime = MutableStateFlow<LocalTime?>(null)
    val startTime = _startTime.asStateFlow()

    private val _endTime = MutableStateFlow<LocalTime?>(null)
    val endTime = _endTime.asStateFlow()

    val dayCodeToDayNameMap = mapOf(
        1 to "senin",
        2 to "selasa",
        3 to "rabu",
        4 to "kamis",
        5 to "jumat",
        6 to "sabtu",
        7 to "minggu"
    )

    fun setDay(value: DropdownOption){
        _day.value = value
    }

    fun setStartTime(value: LocalTime){
        _startTime.value = value
    }

    fun setEndTime(value: LocalTime){
        _endTime.value = value
    }

    private fun validateClinicSchedule(): Boolean {
        if(day.value == null){
            setMessage("Hari wajib diinput")
            return false
        } else if(startTime.value == null){
            setMessage("Waktu mulai wajib diinput")
            return false
        }else if(endTime.value == null){
            setMessage("Waktu berakhir wajib diinput")
            return false
        }
        return true
    }

    suspend fun getClinicScheduleDetail(clinicScheduleId: Int){
        val response = viewModelScope.async(Dispatchers.IO) {
            clinicScheduleRepository.getClinicScheduleDetail(clinicScheduleId)
        }.await()
        when (response) {
            is Call.Success -> {
                val data = response.data?.body()?.data
                if (data !== null) {
                    val day = dayCodeToDayNameMap[data.day]
                    if(day !== null) {
                        _day.value = DropdownOption("0", day.replaceFirstChar { it.uppercase() })
                        _startTime.value = LocalTime.parse(data.startTime)
                        _endTime.value = LocalTime.parse(data.endTime)
                    }
                }
            }
            else -> setMessage(response.data?.message() as String)
        }
    }

    suspend fun updateClinicSchedule(clinicScheduleId: Int): Boolean {
        if(validateClinicSchedule()) {
            val response = viewModelScope.async(Dispatchers.IO) {
                clinicScheduleRepository.updateClinicScheduleList(
                    clinicScheduleId,
                    UpdateClinicScheduleBody(
                        day = null,
                        shift = null,
                        startTime = startTime.value?.format(DateTimeFormatter.ISO_LOCAL_TIME),
                        endTime = endTime.value?.format(DateTimeFormatter.ISO_LOCAL_TIME)
                    )
                )
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

    suspend fun deleteClinicSchedule(clinicScheduleId: Int): Boolean {
        if(validateClinicSchedule()) {
            val response = viewModelScope.async(Dispatchers.IO) {
                clinicScheduleRepository.deleteClinicScheduleList(clinicScheduleId)
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

    suspend fun addClinicSchedule(): Boolean {
        if(validateClinicSchedule()) {
            val userId = userRepository.getUserId()
            if (userId !== null && day.value !== null && startTime.value != null && endTime.value != null) {
                val day = day.value
                val response = viewModelScope.async(Dispatchers.IO) {
                    clinicScheduleRepository.addClinicSchedule(CreateClinicScheduleBody(
                        petClinicId = userId,
                        day = day?.id?.toInt() ?: 0,
                        startTime = startTime.value?.format(DateTimeFormatter.ISO_LOCAL_TIME) ?: "",
                        endTime = endTime.value?.format(DateTimeFormatter.ISO_LOCAL_TIME) ?: ""
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
        return false
    }
}