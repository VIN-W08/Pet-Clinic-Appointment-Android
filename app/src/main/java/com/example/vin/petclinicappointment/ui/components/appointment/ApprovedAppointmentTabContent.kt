package com.example.vin.petclinicappointment.ui.components.appointment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.MedicalServices
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vin.petclinicappointment.data.model.AppointmentDetail
import com.example.vin.petclinicappointment.ui.components.common.*
import com.example.vin.petclinicappointment.ui.theme.Gray
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@Composable
fun ApprovedAppointmentTabContent(
    navigateToAppointmentDetail: (id: Int) -> Unit,
    appointmentViewModel: AppointmentViewModel = hiltViewModel(),
){
    var progressIndicatorVisible by rememberSaveable { mutableStateOf(false) }
    val clinicApprovedAppointmentList by appointmentViewModel.clinicApprovedAppointmentList.collectAsState()

    LaunchedEffect(Unit){
        progressIndicatorVisible = true
        appointmentViewModel.getClinicApprovedAppointmentList()
        progressIndicatorVisible = false
    }

    Surface(
        color = PetClinicAppointmentTheme.colors.background
    ){
        Column(
            Modifier.fillMaxSize()
        ) {
            if(clinicApprovedAppointmentList.isEmpty() && !progressIndicatorVisible) {
                MessageView(
                    message = "Tidak ada persetujuan janji temu",
                    modifier = Modifier.fillMaxSize()
                )
            }else {
                ApprovedAppointmentList(
                    appointmentList = clinicApprovedAppointmentList,
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

@Composable
fun ApprovedAppointmentList(
    appointmentList: List<AppointmentDetail>,
    navigateToAppointmentDetail: (appointmentId: Int) -> Unit
){
    LazyColumn(
        Modifier.fillMaxSize()
    ) {
        items(appointmentList) {
            ApprovedAppointmentItem(it, navigateToAppointmentDetail)
        }
    }
}

@Composable
fun ApprovedAppointmentItem(
    appointment: AppointmentDetail,
    navigateToAppointmentDetail: (appointmentId: Int) -> Unit
){
    val startSchedule = LocalDateTime.parse(appointment.serviceSchedule.startSchedule)
    val endSchedule = LocalDateTime.parse(appointment.serviceSchedule.endSchedule)
    val startDate = startSchedule.toLocalDate()
    val startTime = startSchedule.format(DateTimeFormatter.ofPattern("HH:mm"))
    val endTime = endSchedule.format(DateTimeFormatter.ofPattern("HH:mm"))

    Column {
        Card(
            Modifier
                .fillMaxWidth()
                .height(PetClinicAppointmentTheme.dimensions.grid_8 * 2)
                .clickable { navigateToAppointmentDetail(appointment.id) },
            elevation = 0.dp
        ) {
            Row(
                Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    Modifier
                        .padding(PetClinicAppointmentTheme.dimensions.grid_2)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        appointment.customer.name ?: "",
                        style = PetClinicAppointmentTheme.typography.h3,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(
                            bottom = PetClinicAppointmentTheme.dimensions.grid_1
                        )
                    )
                    Column {
                        Row(
                            Modifier
                                .padding(bottom = PetClinicAppointmentTheme.dimensions.grid_0_25),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.MedicalServices,
                                contentDescription = "calendar icon",
                                modifier = Modifier
                                    .padding(end = PetClinicAppointmentTheme.dimensions.grid_1)
                                    .size(PetClinicAppointmentTheme.dimensions.grid_2_5),
                                tint = Gray
                            )
                            Text(appointment.service.name,
                                style = PetClinicAppointmentTheme.typography.body1,
                                fontWeight = FontWeight.SemiBold)
                        }
                        Row(
                            Modifier
                                .padding(bottom = PetClinicAppointmentTheme.dimensions.grid_0_25),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.CalendarToday,
                                contentDescription = "calendar icon",
                                modifier = Modifier
                                    .padding(end = PetClinicAppointmentTheme.dimensions.grid_1)
                                    .size(PetClinicAppointmentTheme.dimensions.grid_2_5),
                                tint = Gray
                            )
                            Text("${startDate.dayOfMonth} ${
                                startDate.month.getDisplayName(TextStyle.FULL,
                                    Locale.getDefault())
                            } ${startDate.year}",
                                style = PetClinicAppointmentTheme.typography.body1,
                                fontWeight = FontWeight.SemiBold)
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Schedule,
                                contentDescription = "clock icon",
                                modifier = Modifier
                                    .padding(end = PetClinicAppointmentTheme.dimensions.grid_1)
                                    .size(PetClinicAppointmentTheme.dimensions.grid_2_5),
                                tint = Gray
                            )
                            Text(
                                "$startTime - $endTime",
                                style = PetClinicAppointmentTheme.typography.body1,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
                StatusLabel(
                    statusCode = appointment.status,
                    hasBackground = true,
                    containerModifier = Modifier.padding(end = PetClinicAppointmentTheme.dimensions.grid_2)
                )
            }
        }
        Divider(Modifier.fillMaxWidth())
    }
}