package com.example.vin.petclinicappointment.ui.components.petclinic

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.NavigateBefore
import androidx.compose.material.icons.rounded.TrendingFlat
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vin.petclinicappointment.data.model.Service
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.vin.petclinicappointment.data.model.ServiceSchedule
import com.example.vin.petclinicappointment.ui.components.common.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.Dispatchers
import com.example.vin.petclinicappointment.ui.theme.*
import kotlinx.coroutines.flow.collectLatest
import java.text.DecimalFormat
import com.example.vin.petclinicappointment.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PetClinicDetailPage(
    petClinicDetailViewModel: PetClinicDetailViewModel = hiltViewModel(),
    id : Int,
    navigateBack: () -> Unit,
    navigateToInfo: (id: Int) -> Unit,
    navigateToHome: (id: Int) -> Unit,
    scaffoldState: ScaffoldState
){
    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { state -> state != ModalBottomSheetValue.HalfExpanded }
    )
    val selectedServiceId by petClinicDetailViewModel.selectedServiceId.collectAsState()
    val clinicDetail by petClinicDetailViewModel.clinicDetail.collectAsState()
    var progressIndicatorVisible by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit){
        petClinicDetailViewModel.message.collectLatest {
            if(it.isNotEmpty()) {
                scaffoldState.snackbarHostState.showSnackbar(it)
            }
        }
    }

    LaunchedEffect(Unit){
        progressIndicatorVisible = true
        petClinicDetailViewModel.getPetClinicDetail(id)
        progressIndicatorVisible = false
    }

    val clinicName = clinicDetail?.name
    val clinicImage = clinicDetail?.image

    Surface (
        Modifier
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    coroutineScope.launch {
                        if (modalBottomSheetState.isVisible) {
                            modalBottomSheetState.hide()
                        }
                    }
                    petClinicDetailViewModel.setSelectedScheduleId(null)
                })
            },
        color = PetClinicAppointmentTheme.colors.background
    ) {
        if(!progressIndicatorVisible){
        if(clinicDetail?.equals(null) == false) {
            Column {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.2f)
                ) {
                    if(clinicImage !== null) {
                        Image(
                            base64 = clinicImage,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            contentDescription = null
                        )
                    }else{
                        Image(
                            painter = painterResource(id = R.drawable.default_clinic_image),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            contentDescription = null
                        )
                    }
                }
                Divider(Modifier.fillMaxWidth())
                Row(
                    Modifier
                        .padding(
                            top = PetClinicAppointmentTheme.dimensions.grid_2,
                            bottom = PetClinicAppointmentTheme.dimensions.grid_2,
                            start = PetClinicAppointmentTheme.dimensions.grid_2,
                            end = PetClinicAppointmentTheme.dimensions.grid_2
                        )
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        clinicName ?: "",
                        style = PetClinicAppointmentTheme.typography.h1,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        icon = Icons.Rounded.Info,
                        contentDescription = "info",
                        onClick = { navigateToInfo(id) },
                        tint = PetClinicAppointmentTheme.colors.primary,
                        modifier = Modifier.size(PetClinicAppointmentTheme.dimensions.grid_3)
                    )
                }
                ModalBottomSheet(
                    Modifier.fillMaxSize(),
                    modalBottomSheetState = modalBottomSheetState,
                    sheetContent = {
                        if (modalBottomSheetState.isVisible) {
                            ServiceScheduleContent(
                                id = selectedServiceId,
                                petClinicDetailViewModel = petClinicDetailViewModel,
                                onClickRegisterAppointment = {
                                    progressIndicatorVisible = true
                                    coroutineScope.launch(Dispatchers.IO) {
                                        petClinicDetailViewModel.createAppointment()
                                    }
                                    navigateToHome(id)
                                    progressIndicatorVisible = false
                                }
                            )
                        } else {
                            petClinicDetailViewModel.setSelectedServiceId(null)
                            petClinicDetailViewModel.setSelectedScheduleId(null)
                            return@ModalBottomSheet
                        }
                    }
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text(
                            "Layanan",
                            style = PetClinicAppointmentTheme.typography.h2,
                            modifier = Modifier.padding(PetClinicAppointmentTheme.dimensions.grid_2)
                        )
                        Divider(Modifier.fillMaxWidth())
                        clinicDetail?.serviceList?.let {
                            ServiceList(
                                serviceList = it,
                                modifier = Modifier.weight(1f),
                                modalBottomSheetState,
                                petClinicDetailViewModel
                            )
                        }
                    }
                }
            }
            Box {
                Row(
                    Modifier.height(PetClinicAppointmentTheme.dimensions.grid_7_5),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        icon = Icons.Rounded.NavigateBefore,
                        contentDescription = "arrow_back",
                        hasBorder = true,
                        onClick = { navigateBack() },
                        containerModifier = Modifier
                            .padding(
                                horizontal = PetClinicAppointmentTheme.dimensions.grid_2
                            )
                            .background(Black_50, CircleShape),
                        modifier = Modifier.padding(end = PetClinicAppointmentTheme.dimensions.grid_0_25),
                        tint = Color.White
                    )
                }
            }
        }
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    visible = progressIndicatorVisible
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ServiceList(
    serviceList: List<Service>,
    modifier: Modifier = Modifier,
    modalBottomSheetState: ModalBottomSheetState,
    petClinicDetailViewModel: PetClinicDetailViewModel
){
    if(serviceList.isNotEmpty()){
        LazyColumn(
            modifier = modifier
        ){
            items(serviceList) { service ->
                ServiceItem(service, modalBottomSheetState, petClinicDetailViewModel)
            }
        }
    }else{
        MessageView(
            message = "Layanan tidak tersedia",
            modifier = Modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ServiceItem(
    service: Service,
    modalBottomSheetState: ModalBottomSheetState,
    petClinicDetailViewModel: PetClinicDetailViewModel
){
    val coroutineScope = rememberCoroutineScope()

    Column {
        Row(
            Modifier
                .clickable {
                    petClinicDetailViewModel.setSelectedServiceId(service.id)
                    coroutineScope.launch {
                        modalBottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
                    }
                }
                .padding(horizontal = PetClinicAppointmentTheme.dimensions.grid_3)
                .fillMaxWidth()
                .height(PetClinicAppointmentTheme.dimensions.grid_2 * 5),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                service.name,
                style = PetClinicAppointmentTheme.typography.h3
            )
            Text(
                "Rp ${DecimalFormat("0.#").format(service.price)}",
                style = PetClinicAppointmentTheme.typography.h3
            )
        }
        Divider(Modifier.fillMaxWidth())
    }
}

@Composable
fun ServiceScheduleContent(
    petClinicDetailViewModel: PetClinicDetailViewModel,
    id: Int?,
    onClickRegisterAppointment: () -> Unit
) {
    val serviceDetail by petClinicDetailViewModel.serviceDetail.collectAsState()
    val selectedScheduleId by petClinicDetailViewModel.selectedScheduleId.collectAsState()
    val serviceName = serviceDetail?.name
    val scheduleList = serviceDetail?.scheduleList

    LaunchedEffect(Unit){
        if(id !== null) {
            petClinicDetailViewModel.getServiceDetail(id)
        }
    }

    if(serviceDetail?.equals(null) == false) {
        Column(
            Modifier.pointerInput(Unit) {
                detectTapGestures(onTap = {
                    petClinicDetailViewModel.setSelectedScheduleId(null)
                })
            }
        ) {
            if (scheduleList != null) {
                if (scheduleList.isNotEmpty()) {
                    Column(
                        Modifier.padding(PetClinicAppointmentTheme.dimensions.grid_2)
                    ) {
                        Text(
                            "Jadwal $serviceName" ?: "",
                            style = PetClinicAppointmentTheme.typography.h2
                        )
                    }
                    DateList(
                        petClinicDetailViewModel,
                        modifier = Modifier.padding(
                            start = PetClinicAppointmentTheme.dimensions.grid_2,
                            bottom = PetClinicAppointmentTheme.dimensions.grid_2
                        )
                    )
                    Divider(Modifier.fillMaxWidth())
                    if (id !== null) {
                        TimeList(
                            modifier = Modifier.weight(1f),
                            petClinicDetailViewModel
                        )

                        Column(
                            Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Divider(Modifier.fillMaxWidth())
                            AppButton(
                                onClick = onClickRegisterAppointment,
                                disabled = selectedScheduleId == null,
                                modifier = Modifier
                                    .padding(PetClinicAppointmentTheme.dimensions.grid_2)
                                    .fillMaxWidth()
                                    .height(PetClinicAppointmentTheme.dimensions.grid_5_5),
                                colors = ButtonDefaults.buttonColors(PetClinicAppointmentTheme.colors.primary),
                                shape = RoundedCornerShape(PetClinicAppointmentTheme.dimensions.grid_5)
                            ) {
                                Text("Daftar")
                            }
                        }
                    } else {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(PetClinicAppointmentTheme.dimensions.grid_2),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Maaf, jadwal layanan $serviceName tidak tersedia.",
                                style = PetClinicAppointmentTheme.typography.h2,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DateList(
    petClinicDetailViewModel: PetClinicDetailViewModel,
    modifier: Modifier = Modifier
){
    val localDate = LocalDate.now()
    LazyRow(
        modifier = modifier
    ) {
        items((0..6).toList()) { item ->
            DateItem(localDate = localDate.plusDays(item.toLong()), petClinicDetailViewModel)
        }
    }
}

@Composable
fun DateItem(
    localDate: LocalDate,
    petClinicDetailViewModel: PetClinicDetailViewModel
){
    val selectedDate by petClinicDetailViewModel.selectedDate.collectAsState()
    val brushColor = if(selectedDate == localDate)
        PetClinicAppointmentTheme.colors.onSecondary
    else PetClinicAppointmentTheme.colors.onSurface
    val backgroundColor = if(selectedDate == localDate)
        PetClinicAppointmentTheme.colors.secondary
    else PetClinicAppointmentTheme.colors.surface

    View(
        onClick = { petClinicDetailViewModel.setSelectedDate(localDate) },
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
            localDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
            fontWeight = FontWeight.SemiBold,
            color = brushColor
        )
    }
}

@Composable
fun TimeList(
    modifier: Modifier = Modifier,
    petClinicDetailViewModel: PetClinicDetailViewModel
){
    val scheduleList by petClinicDetailViewModel.scheduleList.collectAsState()
    val selectedDate by petClinicDetailViewModel.selectedDate.collectAsState()

    LaunchedEffect(selectedDate){
        petClinicDetailViewModel.getServiceScheduleList()
    }

    if(scheduleList.isNotEmpty()) {
        LazyColumn(
            modifier = modifier
        ) {
            items(scheduleList) {
                TimeItem(it, petClinicDetailViewModel)
            }
        }
    }else{
        MessageView(
            message = "Jadwal layanan tidak tersedia",
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun TimeItem(
    schedule: ServiceSchedule,
    petClinicDetailViewModel: PetClinicDetailViewModel
){
    val selectedScheduleId by petClinicDetailViewModel.selectedScheduleId.collectAsState()
    val brushColor = if(selectedScheduleId == schedule.id)
                        PetClinicAppointmentTheme.colors.onSecondary
                    else if(!schedule.status) Color.Gray
                    else PetClinicAppointmentTheme.colors.onSurface
    val backgroundColor = if(selectedScheduleId == schedule.id)
                            PetClinicAppointmentTheme.colors.secondary
                            else if(!schedule.status) Platinum
                            else PetClinicAppointmentTheme.colors.surface

    Column {
        View(
            Modifier
                .fillMaxWidth()
                .height(PetClinicAppointmentTheme.dimensions.grid_2 * 3),
            contentAlignment = Alignment.Center,
            onClick = {
                if (schedule.status) {
                    petClinicDetailViewModel.setSelectedScheduleId(schedule.id)
                } else return@View
            }
        ) {
            Row(
                Modifier
                    .fillMaxSize()
                    .background(backgroundColor),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    LocalDateTime.parse(schedule.startSchedule).format(DateTimeFormatter.ofPattern("HH:mm")),
                    style = PetClinicAppointmentTheme.typography.body1,
                    fontSize = PetClinicAppointmentTheme.typography.h2.fontSize,
                    color = brushColor
                )
                Spacer(modifier = Modifier.width(PetClinicAppointmentTheme.dimensions.grid_3))
                Icon(
                    imageVector = Icons.Rounded.TrendingFlat,
                    contentDescription = "to",
                    modifier = Modifier.size(PetClinicAppointmentTheme.dimensions.grid_4),
                    tint = brushColor
                )
                Spacer(modifier = Modifier.width(PetClinicAppointmentTheme.dimensions.grid_3))
                Text(
                    LocalDateTime.parse(schedule.endSchedule).format(DateTimeFormatter.ofPattern("HH:mm")),
                    style = PetClinicAppointmentTheme.typography.body1,
                    fontSize = PetClinicAppointmentTheme.typography.h2.fontSize,
                    color = brushColor
                )
            }
        }
        Divider(Modifier.fillMaxWidth())
    }
}