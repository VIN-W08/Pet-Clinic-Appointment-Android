package com.example.vin.petclinicappointment.ui.components.appointment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vin.petclinicappointment.ui.components.common.CircularProgressIndicator
import com.example.vin.petclinicappointment.ui.components.common.MessageView
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme

@Composable
fun RequestingAppointmentTabContent(
    navigateToAppointmentDetail: (id: Int) -> Unit,
    appointmentViewModel: AppointmentViewModel = hiltViewModel(),
){
    var progressIndicatorVisible by rememberSaveable { mutableStateOf(false) }
    val clinicRequestingAppointmentList by appointmentViewModel.clinicRequestingAppointmentList.collectAsState()

    LaunchedEffect(Unit){
        progressIndicatorVisible = true
        appointmentViewModel.getClinicRequestingAppointmentList()
        progressIndicatorVisible = false
    }

    Surface(
        color = PetClinicAppointmentTheme.colors.background
    ){
        Column(
            Modifier.fillMaxSize()
        ) {
            if(clinicRequestingAppointmentList.isEmpty() && !progressIndicatorVisible) {
                MessageView(
                    message = "Tidak ada pengajuan janji temu",
                    modifier = Modifier.fillMaxSize()
                )
            }else {
                AppointmentList(
                    appointmentList = clinicRequestingAppointmentList,
                    navigateToAppointmentDetail = navigateToAppointmentDetail
                )
            }
        }
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(visible = progressIndicatorVisible)
        }
    }
}