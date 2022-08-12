package com.example.vin.petclinicappointment.ui.components.appointment

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import com.example.vin.petclinicappointment.R

enum class AppointmentTab(
    @StringRes val title: Int,
    val view: @Composable (
        navigateToAppointmentDetail: (id: Int) -> Unit,
        currentPage: Int
    ) -> Unit
) {
    Request(R.string.request, {navigateToAppointmentDetail, currentPage -> RequestingAppointmentTabContent(navigateToAppointmentDetail, currentPage) }),
    Approved(R.string.approved, {navigateToAppointmentDetail, currentPage -> ApprovedAppointmentTabContent(navigateToAppointmentDetail, currentPage) })
}