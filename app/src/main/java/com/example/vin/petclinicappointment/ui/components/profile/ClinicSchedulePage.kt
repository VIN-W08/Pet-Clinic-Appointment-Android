package com.example.vin.petclinicappointment.ui.components.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vin.petclinicappointment.data.model.ClinicSchedule
import com.example.vin.petclinicappointment.ui.components.common.AppButton
import com.example.vin.petclinicappointment.ui.components.common.CircularProgressIndicator
import com.example.vin.petclinicappointment.ui.components.common.IconButton
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme

@Composable
fun ClinicSchedulePage(
    clinicScheduleViewModel: ClinicScheduleViewModel = hiltViewModel(),
    navigateToClinicScheduleAdd: () -> Unit,
    navigateToClinicScheduleUpdate: (clinicScheduleId: Int) -> Unit,
    navigateBack: () -> Unit
){
    val clinicScheduleList by clinicScheduleViewModel.clinicScheduleList.collectAsState()
    var progressIndicatorVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit){
        progressIndicatorVisible = true
        clinicScheduleViewModel.getClinicScheduleList()
        progressIndicatorVisible = false
    }

    Surface(
        color = PetClinicAppointmentTheme.colors.background
    ) {
        Column(
            Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                    "Jadwal Operasi Klinik",
                    style = PetClinicAppointmentTheme.typography.h1
                )
            }
            Column {
                Row (
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ){
                    AppButton(
                        onClick = { navigateToClinicScheduleAdd() },
                        colors = ButtonDefaults.buttonColors(PetClinicAppointmentTheme.colors.secondary),
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
                ClinicScheduleDayListView(
                    clinicScheduleList = clinicScheduleList,
                    dayCodeToDayNameMap = clinicScheduleViewModel.dayCodeToDayNameMap,
                    navigateToClinicScheduleUpdate = navigateToClinicScheduleUpdate
                )
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
fun ClinicScheduleDayListView(
    clinicScheduleList: List<ClinicSchedule>,
    dayCodeToDayNameMap: Map<Int, String>,
    navigateToClinicScheduleUpdate: (clinicScheduleId: Int) -> Unit
){
    Column {
        Divider(Modifier.fillMaxWidth())
        LazyColumn {
            items((1..7).toList()) { idx ->
                val dayName = dayCodeToDayNameMap[idx]
                if(dayName !== null) {
                    ClinicScheduleDayItemView(
                        dayName = dayName.replaceFirstChar { it.uppercase() },
                        clinicScheduleList = clinicScheduleList
                            .filter { schedule -> schedule.day == idx },
                        navigateToClinicScheduleUpdate = navigateToClinicScheduleUpdate
                    )
                }
            }
        }
    }
}

@Composable
fun ClinicScheduleDayItemView(
    dayName: String,
    clinicScheduleList: List<ClinicSchedule>,
    navigateToClinicScheduleUpdate: (clinicScheduleId: Int) -> Unit
){
    Column {
        Column(
            Modifier.fillMaxWidth()
        ) {
            Row(
                Modifier
                    .padding(PetClinicAppointmentTheme.dimensions.grid_2)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    dayName,
                    style = PetClinicAppointmentTheme.typography.h3
                )
                if (clinicScheduleList.isEmpty()) {
                    Text("-")
                }
            }
            if(clinicScheduleList.isEmpty()) {
                Divider(Modifier.fillMaxWidth())
            }
        }
        if(clinicScheduleList.isNotEmpty()) {
            ClinicScheduleTimeListView(
                clinicScheduleList = clinicScheduleList,
                navigateToClinicScheduleUpdate = navigateToClinicScheduleUpdate
            )
        }
    }
}

@Composable
fun ClinicScheduleTimeListView(
    clinicScheduleList: List<ClinicSchedule>,
    navigateToClinicScheduleUpdate: (clinicScheduleId: Int) -> Unit
){
    Column {
        Divider(Modifier.fillMaxWidth())
        clinicScheduleList.map { schedule ->
            ClinicScheduleTimeItemView(
                "${schedule.startTime} - ${schedule.endTime}",
                navigateToClinicScheduleUpdate = { navigateToClinicScheduleUpdate(schedule.id) }
            )
        }
    }
}

@Composable
fun ClinicScheduleTimeItemView(
    timeRange: String,
    navigateToClinicScheduleUpdate: () -> Unit
){
    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .clickable { navigateToClinicScheduleUpdate() }
        ) {
            Text(
                timeRange,
                style = PetClinicAppointmentTheme.typography.h3,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(
                    horizontal = PetClinicAppointmentTheme.dimensions.grid_2,
                    vertical = PetClinicAppointmentTheme.dimensions.grid_1
                )
            )
        }
        Divider(Modifier.fillMaxWidth())
    }
}