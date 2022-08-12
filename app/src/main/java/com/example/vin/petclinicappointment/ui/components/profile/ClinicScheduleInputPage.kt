package com.example.vin.petclinicappointment.ui.components.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vin.petclinicappointment.ui.components.common.*
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun ClinicScheduleInputPage(
    id: Int? = null,
    pageType: String,
    navigateBack: () -> Unit,
    navigateToClinicSchedule: () -> Unit,
    clinicScheduleInputViewModel: ClinicScheduleInputViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState
){
    var progressIndicatorVisible by rememberSaveable { mutableStateOf(false) }
    val day by clinicScheduleInputViewModel.day.collectAsState()
    val startTime by clinicScheduleInputViewModel.startTime.collectAsState()
    val endTime by clinicScheduleInputViewModel.endTime.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    fun onDeleteClinicSchedule(){
        if(id !==null) {
            coroutineScope.launch {
                progressIndicatorVisible = true
                val isSuccess = clinicScheduleInputViewModel.deleteClinicSchedule(id)
                progressIndicatorVisible = false
                if (isSuccess) {
                    navigateToClinicSchedule()
                }
            }
        }
    }

    fun onUpdateClinicSchedule(){
        if(id !==null) {
            coroutineScope.launch {
                progressIndicatorVisible = true
                val isSuccess = clinicScheduleInputViewModel.updateClinicSchedule(id)
                progressIndicatorVisible = false
                if (isSuccess) {
                    navigateToClinicSchedule()
                }
            }
        }
    }

    fun onAddClinicSchedule(){
        coroutineScope.launch {
            progressIndicatorVisible = true
            val isSuccess = clinicScheduleInputViewModel.addClinicSchedule()
            progressIndicatorVisible = false
            if (isSuccess) {
                navigateToClinicSchedule()
            }
        }
    }

    LaunchedEffect(Unit){
        clinicScheduleInputViewModel.message.collectLatest {
            if(it.isNotEmpty()) {
                scaffoldState.snackbarHostState.showSnackbar(it)
            }
        }
    }

    LaunchedEffect(Unit){
        if(id !==null) {
            clinicScheduleInputViewModel.getClinicScheduleDetail(id)
        }
    }

    Surface(
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
                    if(pageType == "add")
                        "Tambah Jadwal Operasi Klinik"
                    else "Ubah Jadwal Operasi Klinik",
                    style = PetClinicAppointmentTheme.typography.h1
                )
            }
            Column(
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                if(pageType == "add"){
                    LabelDropdown(
                        label = "Hari",
                        option = day ?: DropdownOption("0", ""),
                        onValueChange = {},
                        optionList = clinicScheduleInputViewModel.dayCodeToDayNameMap.toList()
                            .map {
                                DropdownOption(
                                    it.first.toString(),
                                    it.second.replaceFirstChar { it.uppercase() },
                                )
                            },
                        onClickOption = { clinicScheduleInputViewModel.setDay(it) },
                        modifier = Modifier
                            .padding(
                                start = PetClinicAppointmentTheme.dimensions.grid_2,
                                end = PetClinicAppointmentTheme.dimensions.grid_2,
                                bottom = PetClinicAppointmentTheme.dimensions.grid_2
                            )
                            .fillMaxWidth()
                    )
                }
                if(pageType == "update"){
                    LabelTextInput(
                        label = "Hari",
                        value = if(day!==null) day?.value ?: "" else "",
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .padding(
                                start = PetClinicAppointmentTheme.dimensions.grid_2,
                                end = PetClinicAppointmentTheme.dimensions.grid_2,
                                bottom = PetClinicAppointmentTheme.dimensions.grid_2
                            )
                            .fillMaxWidth()
                    )
                }
                LabelTimeInput(
                    label = "Waktu Mulai",
                    value = if(startTime !== null) startTime.toString() else "",
                    onTimeValueChange = { clinicScheduleInputViewModel.setStartTime(it) },
                    modifier = Modifier
                        .padding(
                            start = PetClinicAppointmentTheme.dimensions.grid_2,
                            end = PetClinicAppointmentTheme.dimensions.grid_2,
                            bottom = PetClinicAppointmentTheme.dimensions.grid_2
                        )
                        .fillMaxWidth()
                )
                LabelTimeInput(
                    label = "Waktu Berakhir",
                    value = if(endTime !== null) endTime.toString() else "",
                    onTimeValueChange = { clinicScheduleInputViewModel.setEndTime(it) },
                    modifier = Modifier
                        .padding(
                            start = PetClinicAppointmentTheme.dimensions.grid_2,
                            end = PetClinicAppointmentTheme.dimensions.grid_2,
                            bottom = PetClinicAppointmentTheme.dimensions.grid_2
                        )
                        .fillMaxWidth()
                )
            }
            Column {
                Divider(Modifier.fillMaxWidth())
                if(pageType !== "add") {
                    AppButton(
                        modifier = Modifier
                            .padding(
                                start = PetClinicAppointmentTheme.dimensions.grid_2,
                                end = PetClinicAppointmentTheme.dimensions.grid_2,
                                top = PetClinicAppointmentTheme.dimensions.grid_2,
                                bottom = PetClinicAppointmentTheme.dimensions.grid_1_5
                            )
                            .fillMaxWidth()
                            .height(PetClinicAppointmentTheme.dimensions.grid_5_5),
                        onClick = { onDeleteClinicSchedule() },
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
                    modifier = Modifier
                        .padding(
                            start = PetClinicAppointmentTheme.dimensions.grid_2,
                            end = PetClinicAppointmentTheme.dimensions.grid_2,
                            bottom = PetClinicAppointmentTheme.dimensions.grid_2,
                            top = if (pageType == "add") PetClinicAppointmentTheme.dimensions.grid_2
                            else 0.dp
                        )
                        .fillMaxWidth()
                        .height(PetClinicAppointmentTheme.dimensions.grid_5_5),
                    onClick = { if(pageType == "add") onAddClinicSchedule() else onUpdateClinicSchedule() },
                    colors = ButtonDefaults.buttonColors(PetClinicAppointmentTheme.colors.primary),
                    shape = RoundedCornerShape(PetClinicAppointmentTheme.dimensions.grid_5)
                ) {
                    Text(
                        if(pageType == "add")
                            "Tambah Jadwal" else "Ubah Jadwal"
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