package com.example.vin.petclinicappointment.ui.components.appointment

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val appointmentRepository: AppointmentRepository,
): BaseViewModel() {

    private val _userId = MutableStateFlow<Int?>(null)
    val userId = _userId.asStateFlow()

    init {
        viewModelScope.launch {
            _userId.value = userRepository.getUserId()
        }
    }

    val requestingAppointmentList: Flow<PagingData<AppointmentDetail>> = Pager(PagingConfig(pageSize = 10, prefetchDistance = 1)) {
        AppointmentDataSource(
            appointmentRepository,
            clinicId = userId.value,
            status = 0,
            sortOrder = "asc-created_at"
        )
    }.flow

    val approvedAppointmentList: Flow<PagingData<AppointmentDetail>> = Pager(PagingConfig(pageSize = 10, prefetchDistance = 1)) {
        AppointmentDataSource(
            appointmentRepository,
            clinicId = userId.value,
            status = 1,
            sortOrder = "asc-start_schedule"
        )
    }.flow
}