package com.example.vin.petclinicappointment.ui.components.home

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.vin.petclinicappointment.data.datasource.AppointmentDataSource
import com.example.vin.petclinicappointment.data.model.AppointmentDetail
import com.example.vin.petclinicappointment.data.repository.AppointmentRepository
import com.example.vin.petclinicappointment.data.repository.UserRepository
import com.example.vin.petclinicappointment.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    private val userRepository: UserRepository,
): BaseViewModel() {

    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()

    private val _userId = MutableStateFlow<Int?>(null)
    val userId = _userId.asStateFlow()

    init {
        viewModelScope.launch {
            _userId.value = userRepository.getUserId()
        }
    }

    suspend fun getUserName() {
        viewModelScope.launch {
            _username.value = userRepository.getUserName() ?: ""
        }
    }

    val todayApprovedAppointmentList: Flow<PagingData<AppointmentDetail>> = Pager(PagingConfig(pageSize = 10, prefetchDistance = 1)) {
        AppointmentDataSource(
            appointmentRepository,
            customerId = userId.value,
            startSchedule = "${LocalDate.now()}T00:00:00",
            status = 1
        )
    }.flow
}