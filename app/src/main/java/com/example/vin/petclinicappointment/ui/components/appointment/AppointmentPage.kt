package com.example.vin.petclinicappointment.ui.components.appointment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vin.petclinicappointment.data.model.Appointment
import com.example.vin.petclinicappointment.ui.components.common.*
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AppointmentPage(
    appointmentViewModel: AppointmentViewModel = hiltViewModel(),
    navigateToAppointmentDetail: (id: Int) -> Unit,
    navigateToClinicAppointmentHistory: () -> Unit,
    scaffoldState: ScaffoldState
){
    val appointmentTabs = listOf(
        AppointmentTab.Approved,
        AppointmentTab.Request
    )
    val pagerState = rememberPagerState(initialPage = 0)

    LaunchedEffect(Unit){
        appointmentViewModel.message.collectLatest {
            if(it.isNotEmpty()) {
                scaffoldState.snackbarHostState.showSnackbar(it)
            }
        }
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
                    "Daftar Janji Temu",
                    style = PetClinicAppointmentTheme.typography.h1
                )
            }
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ){
                Text(
                    "Riwayat",
                    style = PetClinicAppointmentTheme.typography.h2,
                    color = PetClinicAppointmentTheme.colors.secondary,
                    modifier = Modifier
                        .padding(
                            end = PetClinicAppointmentTheme.dimensions.grid_2,
                            bottom = PetClinicAppointmentTheme.dimensions.grid_1
                        )
                        .clickable { navigateToClinicAppointmentHistory() }
                )
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                AppointmentTabRow(tabs = appointmentTabs, pagerState = pagerState)
                AppointmentTabContent(tabs = appointmentTabs, pagerState = pagerState, navigateToAppointmentDetail = navigateToAppointmentDetail)
            }
        }
    }
}

@Composable
fun AppointmentList(
    title: String? = null,
    appointmentList: List<Appointment>,
    navigateToAppointmentDetail: (id: Int) -> Unit
){
    Column(
        Modifier
            .fillMaxWidth()
    ) {
        if(!title.isNullOrEmpty()) {
            Text(
                title,
                style = PetClinicAppointmentTheme.typography.h2,
                modifier = Modifier
                    .padding(
                        start = PetClinicAppointmentTheme.dimensions.grid_2,
                        bottom = PetClinicAppointmentTheme.dimensions.grid_1
                    )
            )
            Divider(Modifier.fillMaxWidth())
        }
        LazyColumn {
            items(appointmentList) {
                AppointmentItem(
                    appointment = it,
                    navigateToAppointmentDetail = navigateToAppointmentDetail
                )
            }
        }
    }
}

@Composable
fun AppointmentItem(
    appointment: Appointment,
    navigateToAppointmentDetail: (id: Int) -> Unit
){
    Column(
        Modifier
            .fillMaxWidth()
            .height(PetClinicAppointmentTheme.dimensions.grid_6 * 2)
            .clickable { navigateToAppointmentDetail(appointment.id) }
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                Modifier
                    .padding(
                        horizontal = PetClinicAppointmentTheme.dimensions.grid_2,
                    )
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    Modifier.fillMaxHeight(0.6f),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "${appointment.customer.name}",
                        style = PetClinicAppointmentTheme.typography.h2,
                    )
                    Text(
                        LocalDateTime.parse(appointment.createdAt)
                            .format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm")),
                    )
                }
                StatusLabel(
                    statusCode = appointment.status,
                    hasBackground = true
                )
            }
        }
        Divider(Modifier.fillMaxWidth())
    }
}