package com.example.vin.petclinicappointment.ui.components.appointment

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import com.example.vin.petclinicappointment.R

enum class AppointmentTab(
    @StringRes val title: Int,
    val view: @Composable (
        navigateToAppointmentDetail: (id: Int) -> Unit
    ) -> Unit
) {
    Request(R.string.request, { RequestingAppointmentTabContent(it) }),
    Approved(R.string.approved, { ApprovedAppointmentTabContent(it) })
}