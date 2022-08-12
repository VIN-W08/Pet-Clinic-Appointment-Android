package com.example.vin.petclinicappointment.ui.components.service

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vin.petclinicappointment.data.model.ServiceSchedule
import com.example.vin.petclinicappointment.ui.components.common.*
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import com.example.vin.petclinicappointment.ui.theme.VeryLightMalachite
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.text.DecimalFormat
import com.example.vin.petclinicappointment.ui.components.common.IconButton

@Composable
fun ServiceDetailPage(
    serviceId: Int,
    serviceDetailViewModel: ServiceDetailViewModel = hiltViewModel(),
    navigateToUpdateService: (id: Int) -> Unit,
    navigateToAddServiceSchedule: (serviceId: Int) -> Unit,
    navigateToServiceScheduleUpdate: (serviceId: Int, serviceScheduleId: Int) -> Unit,
    navigateBack: () -> Unit,
    scaffoldState: ScaffoldState
){
    var progressIndicatorVisible by rememberSaveable { mutableStateOf(false) }
    var expandServiceSchedule by rememberSaveable { mutableStateOf(false) }
    val service by serviceDetailViewModel.service.collectAsState()
    val serviceName = service?.name
    val servicePrice = service?.price
    val serviceStatus = service?.status

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
        if(service !== null) {
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
                        Card(
                            Modifier
                                .padding(
                                    start = PetClinicAppointmentTheme.dimensions.grid_2,
                                    end = PetClinicAppointmentTheme.dimensions.grid_2,
                                    bottom = PetClinicAppointmentTheme.dimensions.grid_4
                                ),
                            elevation = PetClinicAppointmentTheme.dimensions.grid_0_5
                        ) {
                            Column {
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    IconButton(
                                        icon = if (expandServiceSchedule) Icons.Rounded.ExpandMore else Icons.Rounded.ExpandLess,
                                        contentDescription = "expand icon",
                                        modifier = Modifier
                                            .padding(
                                                top = PetClinicAppointmentTheme.dimensions.grid_2,
                                                end = PetClinicAppointmentTheme.dimensions.grid_2
                                            ),
                                        onClick = { expandServiceSchedule = !expandServiceSchedule }
                                    )
                                }
                                Column {
                                    ServiceTextAttributeView(
                                        label = "Nama Layanan",
                                        value = serviceName
                                    )
                                    if(!expandServiceSchedule) {
                                        ServiceTextAttributeView(
                                            label = "Harga Layanan",
                                            value = "Rp ${DecimalFormat("0.#").format(servicePrice)}"
                                        )
                                        if (serviceStatus != null) {
                                            ServiceStatusAttributeView(
                                                label = "Status",
                                                value = serviceStatus
                                            )
                                        }
                                    }
                                }
                                if(!expandServiceSchedule) {
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
                        }
                    ServiceScheduleSectionView(
                        serviceId = serviceId,
                        serviceDetailViewModel = serviceDetailViewModel,
                        navigateToAddServiceSchedule = navigateToAddServiceSchedule,
                        navigateToServiceScheduleUpdate = { serviceScheduleId -> navigateToServiceScheduleUpdate(serviceId, serviceScheduleId)}
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
fun ServiceScheduleSectionView(
    serviceId: Int,
    serviceDetailViewModel: ServiceDetailViewModel,
    navigateToServiceScheduleUpdate: (serviceScheduleId: Int) -> Unit,
    navigateToAddServiceSchedule: (serviceId: Int) -> Unit
){
    val currentDate = LocalDate.now()
    var startListDate by rememberSaveable { mutableStateOf(currentDate) }

    LaunchedEffect(startListDate){
        serviceDetailViewModel.setSelectedServiceStartDate(startListDate)
    }

    Column {
        Row(
            Modifier
                .padding(
                    start = PetClinicAppointmentTheme.dimensions.grid_2,
                    end = PetClinicAppointmentTheme.dimensions.grid_2,
                    bottom = PetClinicAppointmentTheme.dimensions.grid_2
                )
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    "Pilih tanggal",
                    style = PetClinicAppointmentTheme.typography.h3,
                    modifier = Modifier.padding(end = PetClinicAppointmentTheme.dimensions.grid_1)
                )
                DatePicker(
                    onDateValueChange = { startListDate = it },
                    containerModifier = Modifier.background(PetClinicAppointmentTheme.colors.primaryVariant, CircleShape),
                    modifier = Modifier.size(PetClinicAppointmentTheme.dimensions.grid_2_5),
                    hasBorder = true,
                    tint = PetClinicAppointmentTheme.colors.onPrimary
                )
            }
            AppButton(
                onClick = { navigateToAddServiceSchedule(serviceId) },
                colors = buttonColors(PetClinicAppointmentTheme.colors.secondary),
                shape = RoundedCornerShape(PetClinicAppointmentTheme.dimensions.grid_2),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AddCircle,
                        contentDescription = "add_circle",
                        modifier = Modifier.padding(end = PetClinicAppointmentTheme.dimensions.grid_1)
                    )
                    Text("Jadwal")
                }
            }
        }
        DateList(
            startListDate = startListDate,
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
            Modifier.weight(0.4f),
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
            Modifier.weight(0.6f),
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
            Modifier.weight(0.4f),
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
            Modifier.weight(0.6f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ServiceStatusLabel(statusCode = value)
        }
    }
}

@Composable
fun DateList(
    startListDate: LocalDate,
    serviceDetailViewModel: ServiceDetailViewModel,
    modifier: Modifier = Modifier
){
    LazyRow(
        modifier = modifier
    ) {
        items((0..6).toList()) { item ->
            DateItem(date = startListDate.plusDays(item.toLong()), serviceDetailViewModel)
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
                        tint = if(schedule.status) VeryLightMalachite else PetClinicAppointmentTheme.colors.error
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