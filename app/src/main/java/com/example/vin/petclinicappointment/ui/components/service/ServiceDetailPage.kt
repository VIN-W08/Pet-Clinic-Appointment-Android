package com.example.vin.petclinicappointment.ui.components.service

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vin.petclinicappointment.data.model.ServiceSchedule
import com.example.vin.petclinicappointment.ui.components.common.*
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.text.DecimalFormat

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ServiceDetailPage(
    serviceId: Int,
    serviceDetailViewModel: ServiceDetailViewModel = hiltViewModel(),
    navigateToUpdateService: (id: Int) -> Unit,
    navigateToAddServiceSchedule: (serviceId: Int) -> Unit,
    navigateToServiceScheduleUpdate: (serviceScheduleId: Int) -> Unit,
    navigateBack: () -> Unit,
    scaffoldState: ScaffoldState
){
    var progressIndicatorVisible by rememberSaveable { mutableStateOf(false) }
    val serviceDetail by serviceDetailViewModel.serviceDetail.collectAsState()
    val serviceName = serviceDetail?.name
    val servicePrice = serviceDetail?.price
    val serviceStatus = serviceDetail?.status

    LaunchedEffect(Unit){
        serviceDetailViewModel.message.collectLatest {
            if(it.isNotEmpty()) {
                scaffoldState.snackbarHostState.showSnackbar(it)
            }
        }
    }

    LaunchedEffect(Unit){
        progressIndicatorVisible = true
        serviceDetailViewModel.setServiceId(serviceId)
        serviceDetailViewModel.getServiceDetail()
        progressIndicatorVisible = false
    }

    Surface(
        color = MaterialTheme.colors.background
    ) {
        if(serviceDetail !== null) {
            Column {
                Row(
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
                ) {
                    Text(
                        "Layanan",
                        style = PetClinicAppointmentTheme.typography.h1
                    )
                }
                Column(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Card (
                        Modifier
                            .padding(
                                start = PetClinicAppointmentTheme.dimensions.grid_2,
                                end = PetClinicAppointmentTheme.dimensions.grid_2,
                                bottom = PetClinicAppointmentTheme.dimensions.grid_4
                            ),
                        elevation = PetClinicAppointmentTheme.dimensions.grid_0_5
                    ){
                        Column {
                            Column {
                                ServiceTextAttributeView(
                                    label = "Nama Layanan",
                                    value = serviceName,
                                    modifier = Modifier.padding(
                                        top = PetClinicAppointmentTheme.dimensions.grid_3
                                    )
                                )
                                ServiceTextAttributeView(label = "Harga Layanan",
                                    value = "Rp ${DecimalFormat("0.#").format(servicePrice)}")
                                if (serviceStatus != null) {
                                    ServiceStatusAttributeView( label = "Status", value = serviceStatus)
                                }
                            }
                            AppButton(
                                onClick = { navigateToUpdateService(serviceId) },
                                colors = buttonColors(PetClinicAppointmentTheme.colors.secondary),
                                shape = RoundedCornerShape(20),
                                modifier = Modifier
                                    .padding(
                                        start = PetClinicAppointmentTheme.dimensions.grid_2,
                                        end = PetClinicAppointmentTheme.dimensions.grid_2,
                                        bottom = PetClinicAppointmentTheme.dimensions.grid_3
                                    )
                                    .fillMaxWidth()
                            ) {
                                Text("Ubah Data")
                            }
                        }
                    }
                    Column {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            AppButton(
                                onClick = { navigateToAddServiceSchedule(serviceId) },
                                colors = buttonColors(PetClinicAppointmentTheme.colors.secondary),
                                shape = RoundedCornerShape(PetClinicAppointmentTheme.dimensions.grid_2),
                                modifier = Modifier
                                    .padding(
                                        end = PetClinicAppointmentTheme.dimensions.grid_2,
                                        bottom = PetClinicAppointmentTheme.dimensions.grid_2
                                    )
                            ) {
                                Row(
                                    modifier = Modifier.width(PetClinicAppointmentTheme.dimensions.grid_9),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.AddCircle,
                                        contentDescription = "add_circle"
                                    )
                                    Text("Jadwal")
                                }
                            }
                        }
                        DateList(
                            serviceDetailViewModel = serviceDetailViewModel,
                            modifier = Modifier.padding(
                                start = PetClinicAppointmentTheme.dimensions.grid_2,
                                bottom = PetClinicAppointmentTheme.dimensions.grid_2
                            )
                        )
                        TimeList(
                            serviceDetailViewModel = serviceDetailViewModel,
                            navigateToServiceScheduleUpdate = navigateToServiceScheduleUpdate
                        )
                    }
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
fun ServiceTextAttributeView(
    label: String,
    value: String?,
    modifier: Modifier = Modifier
){
    Row (
        modifier = modifier
            .padding(
                bottom = PetClinicAppointmentTheme.dimensions.grid_4
            )
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                label,
                style = PetClinicAppointmentTheme.typography.h3,
                modifier = Modifier.padding(
                    horizontal = PetClinicAppointmentTheme.dimensions.grid_2
                )
            )
        }
        Row(
            Modifier.width(PetClinicAppointmentTheme.dimensions.grid_10 * 3),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                value ?: "-",
                style = PetClinicAppointmentTheme.typography.h3,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
fun ServiceStatusAttributeView(
    label: String,
    value: Boolean,
    modifier: Modifier = Modifier
){
    Row (
        modifier = modifier
            .padding(
                bottom = PetClinicAppointmentTheme.dimensions.grid_4
            )
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                label,
                style = PetClinicAppointmentTheme.typography.h3,
                modifier = Modifier.padding(
                    horizontal = PetClinicAppointmentTheme.dimensions.grid_2
                )
            )
        }
        Row(
            Modifier.width(PetClinicAppointmentTheme.dimensions.grid_10 * 3),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ServiceStatusLabel(statusCode = value)
        }
    }
}

@Composable
fun DateList(
    serviceDetailViewModel: ServiceDetailViewModel,
    modifier: Modifier = Modifier
){
    val currentLocalDate = LocalDate.now()
    LazyRow(
        modifier = modifier
    ) {
        items((0..6).toList()) { item ->
            DateItem(date = currentLocalDate.plusDays(item.toLong()), serviceDetailViewModel)
        }
    }
}

@Composable
fun DateItem(
    date: LocalDate,
    serviceDetailViewModel: ServiceDetailViewModel
){
    val selectedDate by serviceDetailViewModel.selectedServiceStartDate.collectAsState()
    val brushColor = if(selectedDate == date)
        PetClinicAppointmentTheme.colors.onPrimary
    else PetClinicAppointmentTheme.colors.onSurface
    val backgroundColor = if(selectedDate == date)
        PetClinicAppointmentTheme.colors.primaryVariant
    else PetClinicAppointmentTheme.colors.surface

    View(
        onClick = { serviceDetailViewModel.setSelectedServiceStartDate(date) },
        modifier = Modifier
            .padding(end = PetClinicAppointmentTheme.dimensions.grid_1_5)
            .clip(RoundedCornerShape(PetClinicAppointmentTheme.dimensions.grid_1))
            .width(PetClinicAppointmentTheme.dimensions.grid_6 * 2)
            .height(PetClinicAppointmentTheme.dimensions.grid_6)
            .border(
                PetClinicAppointmentTheme.dimensions.grid_0_125,
                brushColor,
                RoundedCornerShape(PetClinicAppointmentTheme.dimensions.grid_1)
            )
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            date.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
            fontWeight = FontWeight.SemiBold,
            color = brushColor
        )
    }
}

@Composable
fun TimeList(
    serviceDetailViewModel: ServiceDetailViewModel,
    navigateToServiceScheduleUpdate: (serviceScheduleId: Int) -> Unit,
    modifier: Modifier = Modifier
){
    val scheduleList by serviceDetailViewModel.serviceScheduleList.collectAsState()
    val selectedDate by serviceDetailViewModel.selectedServiceStartDate.collectAsState()
    var progressIndicatorVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(selectedDate){
        progressIndicatorVisible = true
        serviceDetailViewModel.getScheduleServiceList()
        progressIndicatorVisible = false
    }

    if(scheduleList.isEmpty() && !progressIndicatorVisible) {
        NoServiceScheduleView(Modifier.fillMaxSize())
    }else {
        LazyColumn(
            modifier = modifier
        ) {
            items(scheduleList) {
                TimeItem(it, navigateToServiceScheduleUpdate)
            }
        }
    }
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(visible = progressIndicatorVisible)
    }
}

@Composable
fun TimeItem(
    schedule: ServiceSchedule,
    navigateToServiceScheduleUpdate: (serviceScheduleId: Int) -> Unit
){
    Column {
        Row (
            Modifier
                .clickable { navigateToServiceScheduleUpdate(schedule.id) }
                .fillMaxWidth()
                .height(PetClinicAppointmentTheme.dimensions.grid_2 * 3),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Row(
                Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Circle,
                        contentDescription = "circle",
                        modifier = Modifier
                            .size(PetClinicAppointmentTheme.dimensions.grid_2),
                        tint = if(schedule.status) Color.Green else PetClinicAppointmentTheme.colors.error
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        LocalDateTime.parse(schedule.startSchedule)
                            .format(DateTimeFormatter.ofPattern("HH:mm")),
                        style = PetClinicAppointmentTheme.typography.body1,
                        fontSize = PetClinicAppointmentTheme.typography.h2.fontSize
                    )
                    Spacer(modifier = Modifier.width(PetClinicAppointmentTheme.dimensions.grid_3))
                    Icon(
                        imageVector = Icons.Rounded.TrendingFlat,
                        contentDescription = "right arrow",
                        modifier = Modifier.size(PetClinicAppointmentTheme.dimensions.grid_4)
                    )
                    Spacer(modifier = Modifier.width(PetClinicAppointmentTheme.dimensions.grid_3))
                    Text(
                        LocalDateTime.parse(schedule.endSchedule)
                            .format(DateTimeFormatter.ofPattern("HH:mm")),
                        style = PetClinicAppointmentTheme.typography.body1,
                        fontSize = PetClinicAppointmentTheme.typography.h2.fontSize
                    )
                }
                Row(
                    modifier = Modifier.width(PetClinicAppointmentTheme.dimensions.grid_3_5),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${schedule.quota}")
                    Icon(
                        imageVector = Icons.Rounded.Person,
                        contentDescription = "person",
                        modifier = Modifier
                            .size(PetClinicAppointmentTheme.dimensions.grid_2),
                        tint = PetClinicAppointmentTheme.colors.onSurface
                    )
                }
            }
        }
        Divider(Modifier.fillMaxWidth())
    }
}