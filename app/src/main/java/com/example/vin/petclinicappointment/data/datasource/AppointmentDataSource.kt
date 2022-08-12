package com.example.vin.petclinicappointment.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.vin.petclinicappointment.data.model.AppointmentDetail
import com.example.vin.petclinicappointment.data.repository.AppointmentRepository
import java.lang.Exception

class AppointmentDataSource(
    private val appointmentRepository: AppointmentRepository  ,
    private val customerId: Int? = null,
    private val clinicId: Int? = null,
    private val status: Int? = null,
    private val startSchedule: String? = null,
    private val fromSchedule: String? = null,
    private val finished: Boolean? = null,
    private val sortOrder: String? = null,
    private val allowPagination: Boolean = true
): PagingSource<Int, AppointmentDetail>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AppointmentDetail> {
        return try {
            val nextPage = if(allowPagination) (params.key ?: 1) else null
            val clinicListResponse = appointmentRepository.getAppointmentList(
                customerId = customerId,
                clinicId = clinicId,
                status = status,
                startSchedule = startSchedule,
                fromSchedule = fromSchedule,
                finished = finished,
                sortOrder = sortOrder,
                pageNumber = nextPage
            )
            val data = clinicListResponse.data?.body()?.data ?: listOf()
            LoadResult.Page(
                data = data,
                prevKey = if (nextPage == 1 || nextPage == null) null else nextPage - 1,
                nextKey = if (data.isEmpty() || nextPage == null) null else nextPage.plus(1)
            )
        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, AppointmentDetail>): Int? {
        return state.anchorPosition
    }
}