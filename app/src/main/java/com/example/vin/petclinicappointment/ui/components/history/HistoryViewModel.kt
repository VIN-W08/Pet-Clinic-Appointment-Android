package com.example.vin.petclinicappointment.ui.components.history

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
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    private val userRepository: UserRepository
): BaseViewModel() {
    private val _userId = MutableStateFlow<Int?>(null)
    val userId = _userId.asStateFlow()

    init {
        viewModelScope.launch {
            _userId.value = userRepository.getUserId()
        }
    }
    val pastAppointmentList: Flow<PagingData<AppointmentDetail>> = Pager(PagingConfig(pageSize = 10, prefetchDistance = 1)) {
        AppointmentDataSource(
            appointmentRepository,
            customerId = userId.value,
            finished = true,
            fromSchedule = "${LocalDate.now().minusMonths(6)}T00:00:00",
            sortOrder = "desc-updated_at"
        )
    }.flow

    val currentAppointmentList: Flow<PagingData<AppointmentDetail>> = Pager(PagingConfig(pageSize = 10)) {
        AppointmentDataSource(
            appointmentRepository,
            customerId = userId.value,
            finished = false,
            allowPagination = false
        )
    }.flow
}