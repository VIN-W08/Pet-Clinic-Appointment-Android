package com.example.vin.petclinicappointment.ui.components.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vin.petclinicappointment.data.model.Appointment
import com.example.vin.petclinicappointment.ui.components.common.*
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.example.vin.petclinicappointment.R

@Composable
fun HistoryPage(
    historyViewModel: HistoryViewModel = hiltViewModel(),
    navigateToAppointmentDetail: (id: Int) -> Unit,
    navigateBack: () -> Unit
){
    val finishedAppointmentList by historyViewModel.finishedAppointmentList.collectAsState()
    val unfinishedAppointmentList by historyViewModel.unfinishedAppointmentList.collectAsState()
    var progressIndicatorVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit){
        progressIndicatorVisible = true
        historyViewModel.getUnfinishedAppointmentList()
        historyViewModel.getFinishedAppointmentList()
        progressIndicatorVisible = false
    }

    Surface (
        color = PetClinicAppointmentTheme.colors.background
    ) {
        Column(
            Modifier.fillMaxSize()
        ) {
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
            Column (
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if(finishedAppointmentList.isEmpty() && unfinishedAppointmentList.isEmpty() && !progressIndicatorVisible){
                    NoAppointmentContent(
                        modifier = Modifier.fillMaxSize()
                    )
                }
                if(unfinishedAppointmentList.isNotEmpty()){
                    AppointmentList(
                        title = "Janji Temu Saat Ini",
                        appointmentList = unfinishedAppointmentList,
                        navigateToAppointmentDetail = navigateToAppointmentDetail
                    )
                }
                if (finishedAppointmentList.isNotEmpty()) {
                    AppointmentList(
                        title = "Janji Temu Lalu",
                        appointmentList = finishedAppointmentList,
                        navigateToAppointmentDetail = navigateToAppointmentDetail
                    )
                }
            }
        }
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(visible = progressIndicatorVisible)
        }
        Box {
            Row (
                Modifier.height(PetClinicAppointmentTheme.dimensions.grid_7_5),
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(
                    icon = Icons.Rounded.ArrowBackIos,
                    contentDescription = "arrow_back",
                    onClick = { navigateBack() },
                    modifier = Modifier
                        .padding(start = PetClinicAppointmentTheme.dimensions.grid_2)
                        .size(PetClinicAppointmentTheme.dimensions.grid_2_5)
                )
            }
        }
    }
}

@Composable
fun AppointmentList(
    title: String,
    appointmentList: List<Appointment>,
    navigateToAppointmentDetail: (id: Int) -> Unit
){
    Column(
        Modifier
            .fillMaxWidth()
            .padding(bottom = PetClinicAppointmentTheme.dimensions.grid_4)
    ) {
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
    View (
        Modifier
            .fillMaxWidth()
            .height(PetClinicAppointmentTheme.dimensions.grid_6 * 2),
        onClick = { navigateToAppointmentDetail(appointment.id) }
    ){
        Column {
            Row(
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (appointment.petClinic.image !== null) {
                    Image(
                        base64 = appointment.petClinic.image,
                        contentScale = ContentScale.Crop,
                        contentDescription = "clinic image",
                        modifier = Modifier
                            .padding(PetClinicAppointmentTheme.dimensions.grid_2)
                            .size(PetClinicAppointmentTheme.dimensions.grid_6)
                            .clip(CircleShape)
                    )
                }else {
                    Image(
                        painter = painterResource(id = R.drawable.default_clinic_image),
                        contentScale = ContentScale.Fit,
                        contentDescription = "clinic image",
                        modifier = Modifier
                            .padding(PetClinicAppointmentTheme.dimensions.grid_2)
                            .size(PetClinicAppointmentTheme.dimensions.grid_6)
                            .clip(CircleShape)
                    )
                }
                Row(
                    Modifier
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
                            "${appointment.petClinic.name}",
                            style = PetClinicAppointmentTheme.typography.h2,
                        )
                        Text(
                            LocalDateTime.parse(appointment.createdAt)
                                .format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm")),
                        )
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
}

@Composable
fun NoAppointmentContent(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Tidak memiliki riwayat janji temu")
    }
}