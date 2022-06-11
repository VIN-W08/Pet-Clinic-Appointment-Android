package com.example.vin.petclinicappointment.ui.components.appointment

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults.buttonColors
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.example.vin.petclinicappointment.R
import com.example.vin.petclinicappointment.ui.components.common.*
import kotlinx.coroutines.launch

@Composable
fun AppointmentDetailPage(
    appointmentDetailViewModel: AppointmentDetailViewModel = hiltViewModel(),
    id : Int,
    navigateBack: () -> Unit
){
    val appointmentDetail by appointmentDetailViewModel.appointmentDetail.collectAsState()
    val userRole by appointmentDetailViewModel.userRole.collectAsState()
    var progressIndicatorVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit){
        progressIndicatorVisible = true
        appointmentDetailViewModel.getAppointmentDetail(id)
        appointmentDetailViewModel.getUserRole()
        progressIndicatorVisible = false
    }

    val appointmentId = appointmentDetail?.id
    val clinicName = appointmentDetail?.petClinic?.name
    val clinicImage = appointmentDetail?.petClinic?.image
    val serviceName = appointmentDetail?.service?.name
    val servicePrice = appointmentDetail?.service?.price
    val serviceStartSchedule = appointmentDetail?.serviceSchedule?.startSchedule
    val serviceEndSchedule = appointmentDetail?.serviceSchedule?.endSchedule
    val appointmentCreatedAt = appointmentDetail?.createdAt
    val appointmentStatusCode = appointmentDetail?.status
    val appointmentNote = appointmentDetail?.note
    val appointmentStatus = appointmentDetailViewModel.appointmentCodeToTextStatusMap[appointmentStatusCode]

    Surface (
        color = PetClinicAppointmentTheme.colors.background
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Row (
                Modifier
                    .fillMaxWidth()
                    .padding(
                        top = PetClinicAppointmentTheme.dimensions.grid_2,
                        bottom = PetClinicAppointmentTheme.dimensions.grid_8
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                if (clinicName != null) {
                    Text(
                        LocalDateTime.parse(appointmentCreatedAt)
                            .format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm")),
                        style = PetClinicAppointmentTheme.typography.h2
                    )
                }
            }
            Column (
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = PetClinicAppointmentTheme.dimensions.grid_6),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row (
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                            ){
                        Text(
                            "ID Janji Temu: $appointmentId",
                            style = PetClinicAppointmentTheme.typography.body2,
                            modifier = Modifier.padding(
                                end = PetClinicAppointmentTheme.dimensions.grid_2,
                                bottom = PetClinicAppointmentTheme.dimensions.grid_0_5
                            )
                        )
                    }
                    Divider(Modifier.fillMaxWidth())
                    Row (
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                            ){
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (clinicImage !== null) {
                                Image(
                                    base64 = clinicImage,
                                    contentScale = ContentScale.Crop,
                                    contentDescription = "clinic image",
                                    modifier = Modifier
                                        .padding(PetClinicAppointmentTheme.dimensions.grid_2)
                                        .size(PetClinicAppointmentTheme.dimensions.grid_6)
                                        .clip(CircleShape)
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.default_clinic_image),
                                    contentScale = ContentScale.Fit,
                                    contentDescription = "default clinic image",
                                    modifier = Modifier
                                        .padding(PetClinicAppointmentTheme.dimensions.grid_2)
                                        .size(PetClinicAppointmentTheme.dimensions.grid_6)
                                        .clip(CircleShape)
                                )
                            }
                            Text(
                                "$clinicName",
                                style = PetClinicAppointmentTheme.typography.h2,
                            )
                        }
                        if (appointmentStatusCode != null) {
                            StatusLabel(
                                statusCode = appointmentStatusCode,
                                hasBackground = true,
                                containerModifier = Modifier.padding(
                                    end = PetClinicAppointmentTheme.dimensions.grid_2
                                )
                            )
                        }
                    }
                    Divider(Modifier.fillMaxWidth())
                }

                if(serviceName!==null && serviceStartSchedule !== null && serviceEndSchedule!==null && servicePrice!==null) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = PetClinicAppointmentTheme.dimensions.grid_6),
                       horizontalAlignment = Alignment.CenterHorizontally,
                       verticalArrangement = Arrangement.Center
                    ) {
                        Divider(Modifier.fillMaxWidth())
                        LazyColumn(
                            Modifier.padding(
                                top = PetClinicAppointmentTheme.dimensions.grid_2,
                                bottom = PetClinicAppointmentTheme.dimensions.grid_2
                            )
                        ) {
                            item {
                                Row (
                                    Modifier.padding(
                                        bottom = PetClinicAppointmentTheme.dimensions.grid_2
                                    )
                                        ){
                                    TableHeader(title = "Layanan", modifier = Modifier.weight(0.25f))
                                    TableHeader(title = "Jadwal Mulai", modifier = Modifier.weight(0.25f))
                                    TableHeader(title = "Jadwal Berakhir", modifier = Modifier.weight(0.25f))
                                    TableHeader(title = "Harga",
                                        modifier = Modifier.weight(0.25f))
                                }
                            }
                            items(listOf(appointmentDetail)) {
                                Row {
                                    TableValue(modifier = Modifier.weight(0.25f)){
                                        Text("$serviceName")
                                    }
                                    TableValue(modifier = Modifier.weight(0.25f)){
                                        Column (
                                            Modifier.fillMaxWidth(),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                                ){
                                            Text(LocalDateTime.parse(serviceStartSchedule).format(DateTimeFormatter.ofPattern("dd-MM-YYYY")))
                                            Text(LocalDateTime.parse(appointmentDetail?.serviceSchedule?.startSchedule).format(DateTimeFormatter.ofPattern("HH:mm")))
                                        }
                                    }
                                    TableValue(modifier = Modifier.weight(0.25f)){
                                        Column (
                                            Modifier.fillMaxWidth(),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ){
                                            Text(LocalDateTime.parse(serviceEndSchedule).format(DateTimeFormatter.ofPattern("dd-MM-YYYY")))
                                            Text(LocalDateTime.parse(appointmentDetail?.serviceSchedule?.endSchedule).format(DateTimeFormatter.ofPattern("HH:mm")))
                                        }
                                    }
                                    TableValue(modifier = Modifier.weight(0.25f)){
                                        Text("$servicePrice")
                                    }
                                }
                            }
                        }
                        Divider(Modifier.fillMaxWidth())
                    }
                }

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = PetClinicAppointmentTheme.dimensions.grid_6),
                    verticalArrangement = Arrangement.Center
                ) {
                    Divider(Modifier.fillMaxWidth())
                    Column(
                        Modifier.padding(PetClinicAppointmentTheme.dimensions.grid_2)
                    ) {
                        Text(
                            "Note",
                            style = PetClinicAppointmentTheme.typography.h3,
                            modifier = Modifier.padding(bottom = PetClinicAppointmentTheme.dimensions.grid_2)
                        )
                        if(appointmentNote !== null){
                            if(appointmentNote.isNotEmpty()) {
                                Text(appointmentNote)
                            }else{
                                Text("-")
                            }
                        }
                    }
                    Divider(Modifier.fillMaxWidth())
                }
            }
            if(!userRole.isNullOrEmpty() && !appointmentStatus.isNullOrEmpty()) {
                Divider(Modifier.fillMaxWidth())
                AppointmentAction(
                    appointmentDetailViewModel = appointmentDetailViewModel
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
            IconButton(
                icon = Icons.Rounded.ArrowBackIos,
                contentDescription = "arrow_back",
                onClick = { navigateBack() },
                modifier = Modifier.padding(PetClinicAppointmentTheme.dimensions.grid_2)
            )
        }
    }
}

@Composable
fun TableHeader(
    title: String,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            title,
            style = PetClinicAppointmentTheme.typography.h3
        )
    }
}

@Composable
fun TableValue(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        content()
    }
}

@Composable
fun AppointmentAction(
    appointmentDetailViewModel: AppointmentDetailViewModel
){
    val appointmentActionList by appointmentDetailViewModel.appointmentActionList.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit){
        appointmentDetailViewModel.getAppointmentActionList()
    }

    Row (
        Modifier.fillMaxWidth()
    ) {
        appointmentActionList.map {
            val statusText = appointmentDetailViewModel.appointmentCodeToTextStatusMap[it]
            if(statusText !== null) {
                AppButton(
                    onClick = {
                        coroutineScope.launch {
                            appointmentDetailViewModel.setSelectedAppointmentStatusId(it)
                            appointmentDetailViewModel.updateAppointmentStatus()
                        }
                    },
                    colors = buttonColors(
                        PetClinicAppointmentTheme.colors.surface
                    ),
                    shape = RectangleShape,
                    modifier = Modifier
                        .padding(PetClinicAppointmentTheme.dimensions.grid_2)
                        .weight(1f)
                        .height(PetClinicAppointmentTheme.dimensions.grid_4_5)
                        .border(
                            PetClinicAppointmentTheme.dimensions.grid_0_125,
                            PetClinicAppointmentTheme.colors.onSurface
                        )
                ) {
                    Text(
                        statusText
                            .trim()
                            .replaceFirstChar { it.uppercase() }
                    )
                }
            }
        }
    }
}