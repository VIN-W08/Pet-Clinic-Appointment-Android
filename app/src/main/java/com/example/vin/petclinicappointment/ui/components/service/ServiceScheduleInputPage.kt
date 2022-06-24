package com.example.vin.petclinicappointment.ui.components.service

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vin.petclinicappointment.ui.components.common.*
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.time.format.TextStyle
import java.util.*

@Composable
fun ServiceScheduleInputPage(
    serviceId: Int? = null,
    serviceScheduleId: Int? = null,
    pageType: String,
    navigateBack: () -> Unit,
    serviceScheduleInputViewModel: ServiceScheduleInputViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState
){
    val localFocusManager = LocalFocusManager.current
    val startDate by serviceScheduleInputViewModel.startDate.collectAsState()
    val startTime by serviceScheduleInputViewModel.startTime.collectAsState()
    val endDate by serviceScheduleInputViewModel.endDate.collectAsState()
    val endTime by serviceScheduleInputViewModel.endTime.collectAsState()
    val quota by serviceScheduleInputViewModel.quota.collectAsState()
    val status by serviceScheduleInputViewModel.status.collectAsState()
    var progressIndicatorVisible by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    fun onClickAddSchedule(){
        coroutineScope.launch {
            progressIndicatorVisible = true
            val isSuccess = serviceScheduleInputViewModel.addServiceSchedule()
            progressIndicatorVisible = false
            if(isSuccess) {
                navigateBack()
            }
        }
    }

    fun onClickDeleteSchedule(){
        coroutineScope.launch {
            progressIndicatorVisible = true
            val isSuccess = serviceScheduleInputViewModel.deleteServiceSchedule()
            progressIndicatorVisible = false
            if(isSuccess) {
                navigateBack()
            }
        }
    }

    fun onClickUpdateSchedule(){
        coroutineScope.launch {
            progressIndicatorVisible = true
            val isSuccess = serviceScheduleInputViewModel.updateServiceSchedule()
            progressIndicatorVisible = false
            if(isSuccess) {
                navigateBack()
            }
        }
    }

    LaunchedEffect(Unit){
        serviceScheduleInputViewModel.message.collectLatest {
            if(it.isNotEmpty()) {
                scaffoldState.snackbarHostState.showSnackbar(it)
            }
        }
    }

    LaunchedEffect(Unit){
        if(serviceScheduleId!==null && pageType == "update") {
            serviceScheduleInputViewModel.setServiceScheduleId(serviceScheduleId)
            serviceScheduleInputViewModel.getServiceScheduleDetail()
            serviceScheduleInputViewModel.populateServiceScheduleData()
        }else if(serviceId !== null && pageType == "add"){
            serviceScheduleInputViewModel.setServiceId(serviceId)
        }
    }

    Surface(
        Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = {
                localFocusManager.clearFocus()
            })
        },
        color = PetClinicAppointmentTheme.colors.background
    ){
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
                    when(pageType){
                        "add" -> "Tambah Jadwal Layanan"
                        "update" -> "Ubah Jadwal Layanan"
                        else -> throw IllegalArgumentException()
                    },
                    style = PetClinicAppointmentTheme.typography.h1
                )
            }
            Column(
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                LabelScheduleInput(
                    label = "Jadwal Mulai",
                    dateValue = if(startDate !== null)
                        "${startDate?.dayOfMonth} ${startDate?.month?.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${startDate?.year}"
                        else "" ,
                    timeValue = if(startTime !== null) startTime.toString() else "",
                    onDateValueChange = { serviceScheduleInputViewModel.setStartDate(it) },
                    onTimeValueChange = { serviceScheduleInputViewModel.setStartTime(it) },
                    minDate = System.currentTimeMillis(),
                    maxDate = System.currentTimeMillis() + (6 * 24 * 60 * 60 * 1000),
                    required = pageType == "add",
                    modifier = Modifier
                        .padding(
                            start = PetClinicAppointmentTheme.dimensions.grid_2,
                            end = PetClinicAppointmentTheme.dimensions.grid_2,
                            bottom = PetClinicAppointmentTheme.dimensions.grid_4
                        )
                        .fillMaxWidth()
                )
                LabelScheduleInput(
                    label = "Jadwal Berakhir",
                    dateValue = if(endDate !== null)
                        "${endDate?.dayOfMonth} ${endDate?.month?.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${endDate?.year}"
                        else "",
                    timeValue = if(endTime !== null) endTime.toString() else "",
                    onDateValueChange = { serviceScheduleInputViewModel.setEndDate(it) },
                    onTimeValueChange = { serviceScheduleInputViewModel.setEndTime(it) },
                    minDate = System.currentTimeMillis(),
                    maxDate = System.currentTimeMillis() + (6 * 24 * 60 * 60 * 1000),
                    required = pageType == "add",
                    modifier = Modifier
                        .padding(
                            start = PetClinicAppointmentTheme.dimensions.grid_2,
                            end = PetClinicAppointmentTheme.dimensions.grid_2,
                            bottom = PetClinicAppointmentTheme.dimensions.grid_4
                        )
                        .fillMaxWidth()
                )
                LabelTextInput(
                    label = "Kuota",
                    value = quota,
                    onValueChange = { serviceScheduleInputViewModel.setQuota(it) },
                    required = pageType == "add",
                    numberOnly = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier
                        .padding(
                            start = PetClinicAppointmentTheme.dimensions.grid_2,
                            end = PetClinicAppointmentTheme.dimensions.grid_2,
                            bottom = PetClinicAppointmentTheme.dimensions.grid_4
                        )
                        .fillMaxWidth()
                )
                if(pageType == "update") {
                    LabelSwitchInput(
                        label = "Status",
                        value = status,
                        onSwitchChange = { serviceScheduleInputViewModel.setStatus(it) },
                        modifier = Modifier
                            .padding(
                                start = PetClinicAppointmentTheme.dimensions.grid_2,
                                end = PetClinicAppointmentTheme.dimensions.grid_2,
                                bottom = PetClinicAppointmentTheme.dimensions.grid_4
                            )
                            .fillMaxWidth()
                    )
                }
            }
            Column {
                Divider(Modifier.fillMaxWidth())
                if(pageType !== "add") {
                    AppButton(
                        onClick = { onClickDeleteSchedule() },
                        modifier = Modifier
                            .padding(
                                start = PetClinicAppointmentTheme.dimensions.grid_2,
                                end = PetClinicAppointmentTheme.dimensions.grid_2,
                                top = PetClinicAppointmentTheme.dimensions.grid_2,
                                bottom = PetClinicAppointmentTheme.dimensions.grid_1_5
                            )
                            .fillMaxWidth()
                            .height(PetClinicAppointmentTheme.dimensions.grid_5_5),
                        colors = ButtonDefaults.buttonColors(PetClinicAppointmentTheme.colors.error),
                        shape = RoundedCornerShape(PetClinicAppointmentTheme.dimensions.grid_5)
                    ) {
                        Text(
                            "Hapus Jadwal",
                            color = PetClinicAppointmentTheme.colors.onError
                        )
                    }
                }
                AppButton(
                    onClick = {
                        when(pageType){
                            "add" -> onClickAddSchedule()
                            "update" -> onClickUpdateSchedule()
                            else -> throw IllegalArgumentException()
                        }
                    },
                    modifier = Modifier
                        .padding(
                            start = PetClinicAppointmentTheme.dimensions.grid_2,
                            end = PetClinicAppointmentTheme.dimensions.grid_2,
                            bottom = PetClinicAppointmentTheme.dimensions.grid_2
                        )
                        .fillMaxWidth()
                        .height(PetClinicAppointmentTheme.dimensions.grid_5_5),
                    colors = ButtonDefaults.buttonColors(PetClinicAppointmentTheme.colors.primary),
                    shape = RoundedCornerShape(PetClinicAppointmentTheme.dimensions.grid_5)
                ) {
                    Text(
                        when(pageType){
                            "add" -> "Tambah Jadwal"
                            "update" -> "Ubah Jadwal"
                            else -> throw IllegalArgumentException()
                        },
                        color = PetClinicAppointmentTheme.colors.onPrimary
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