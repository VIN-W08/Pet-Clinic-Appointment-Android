package com.example.vin.petclinicappointment.ui.components.appointment

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vin.petclinicappointment.ui.components.common.CircularProgressIndicator
import com.example.vin.petclinicappointment.ui.components.common.IconButton
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme

@Composable
fun ClinicAppointmentHistoryPage(
    clinicAppointmentHistoryViewModel: ClinicAppointmentHistoryViewModel = hiltViewModel(),
    navigateToAppointmentDetail: (id: Int) -> Unit,
    navigateBack: () -> Unit
){
    val clinicFinishedAppointmentList by clinicAppointmentHistoryViewModel.clinicFinishedAppointmentList.collectAsState()
    var progressIndicatorVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit){
        progressIndicatorVisible = true
        clinicAppointmentHistoryViewModel.getClinicFinishedAppointmentList()
        progressIndicatorVisible = false
    }

    Surface(
        color = MaterialTheme.colors.background
    ) {
        Column {
            Row (
                Modifier
                    .fillMaxWidth()
                    .padding(
                        start = PetClinicAppointmentTheme.dimensions.grid_2,
                        end = PetClinicAppointmentTheme.dimensions.grid_2,
                        top = PetClinicAppointmentTheme.dimensions.grid_2,
                        bottom = PetClinicAppointmentTheme.dimensions.grid_4
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    "Riwayat",
                    style = PetClinicAppointmentTheme.typography.h1
                )
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if(clinicFinishedAppointmentList.isNotEmpty() && !progressIndicatorVisible) {
                    AppointmentList(
                        title = "Janji Temu Lalu",
                        appointmentList = clinicFinishedAppointmentList,
                        navigateToAppointmentDetail = navigateToAppointmentDetail
                    )
                } else NoAppointmentContent(modifier = Modifier.fillMaxSize())
            }
        }
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(visible = progressIndicatorVisible)
        }
        Box {
            IconButton(
                icon = Icons.Rounded.ArrowBackIos,
                contentDescription = "arrow_back",
                onClick = { navigateBack() },
                modifier = Modifier.padding(PetClinicAppointmentTheme.dimensions.grid_2)
            )
        }
    }
}



