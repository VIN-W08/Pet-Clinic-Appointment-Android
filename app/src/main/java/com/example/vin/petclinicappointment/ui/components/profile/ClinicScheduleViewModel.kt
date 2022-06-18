package com.example.vin.petclinicappointment.ui.components.profile

import androidx.lifecycle.viewModelScope
import com.example.vin.petclinicappointment.data.model.Call
import com.example.vin.petclinicappointment.data.model.ClinicSchedule
import com.example.vin.petclinicappointment.data.repository.ClinicScheduleRepository
import com.example.vin.petclinicappointment.data.repository.UserRepository
import com.example.vin.petclinicappointment.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ClinicScheduleViewModel @Inject constructor(
    private val clinicScheduleRepository: ClinicScheduleRepository,
    private val userRepository: UserRepository
): BaseViewModel() {

    private val _clinicScheduleList = MutableStateFlow<List<ClinicSchedule>>(listOf())
    val clinicScheduleList = _clinicScheduleList.asStateFlow()

    val dayCodeToDayNameMap = mapOf(
        1 to "senin",
        2 to "selasa",
        3 to "rabu",
        4 to "kamis",
        5 to "jumat",
        6 to "sabtu",
        7 to "minggu"
    )

    suspend fun getClinicScheduleList(){
        val userId = userRepository.getUserId()
        if(userId !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                clinicScheduleRepository.getClinicScheduleList(userId, null)
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()?.data
                    if (data !== null) {
                        _clinicScheduleList.value = data
                    }
                }
                else -> {
                    setMessage(response.data?.message() as String)
                }
            }
        }
    }
}