package com.example.vin.petclinicappointment.ui.components.appointment

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Divider
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.example.vin.petclinicappointment.ui.components.common.*
import com.example.vin.petclinicappointment.ui.theme.Gray
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import java.text.DecimalFormat

@Composable
fun AppointmentDetailPage(
    appointmentDetailViewModel: AppointmentDetailViewModel = hiltViewModel(),
    id : Int,
    navigateBack: () -> Unit,
    scaffoldState: ScaffoldState
){
    val appointmentDetail by appointmentDetailViewModel.appointmentDetail.collectAsState()
    val userRole by appointmentDetailViewModel.userRole.collectAsState()
    var progressIndicatorVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit){
        appointmentDetailViewModel.message.collectLatest {
            if(it.isNotEmpty()) {
                scaffoldState.snackbarHostState.showSnackbar(it)
            }
        }
    }

    LaunchedEffect(Unit){
        progressIndicatorVisible = true
        appointmentDetailViewModel.getAppointmentDetail(id)
        appointmentDetailViewModel.getUserRole()
        progressIndicatorVisible = false
    }

    fun onClickAppointmentAction(statusId: Int){
        runBlocking {
            progressIndicatorVisible = true
            appointmentDetailViewModel.setSelectedAppointmentStatusId(statusId)
            appointmentDetailViewModel.updateAppointmentStatus()
            progressIndicatorVisible = false
        }
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
    val customerName = appointmentDetail?.customer?.name
    val customerEmail = appointmentDetail?.customer?.email
    val appointmentStatus = appointmentDetailViewModel.appointmentCodeToTextMap[appointmentStatusCode]

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
                        start = PetClinicAppointmentTheme.dimensions.grid_2,
                        end = PetClinicAppointmentTheme.dimensions.grid_2,
                        top = PetClinicAppointmentTheme.dimensions.grid_2,
                        bottom = PetClinicAppointmentTheme.dimensions.grid_4
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                if(clinicName !== null) {
                    Text(
                        LocalDateTime.parse(appointmentCreatedAt)
                            .format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm")),
                        style = PetClinicAppointmentTheme.typography.h1
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
                            if (userRole == "customer" && clinicImage !== null) {
                                Image(
                                    base64 = clinicImage,
                                    contentScale = ContentScale.Crop,
                                    contentDescription = "clinic image",
                                    modifier = Modifier
                                        .padding(PetClinicAppointmentTheme.dimensions.grid_2)
                                        .size(PetClinicAppointmentTheme.dimensions.grid_6)
                                        .clip(CircleShape)
                                )
                            }
                            if(userRole == "customer") {
                                Text(
                                    "$clinicName",
                                    style = PetClinicAppointmentTheme.typography.h3,
                                )
                            }else if(userRole == "clinic"){
                                    Column(
                                        modifier = Modifier
                                            .padding(
                                                horizontal = PetClinicAppointmentTheme.dimensions.grid_2,
                                                vertical = PetClinicAppointmentTheme.dimensions.grid_2
                                            ),
                                        verticalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            "Pelanggan:",
                                            style = PetClinicAppointmentTheme.typography.body2,
                                            modifier = Modifier.padding(
                                                bottom = PetClinicAppointmentTheme.dimensions.grid_2
                                            )
                                        )
                                        Column(
                                            verticalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                "$customerName",
                                                style = PetClinicAppointmentTheme.typography.h3,
                                            )
                                            Text(
                                                "$customerEmail",
                                                style = PetClinicAppointmentTheme.typography.body1,
                                                color = Gray
                                            )
                                        }
                                    }
                            }
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
                            Modifier.padding(PetClinicAppointmentTheme.dimensions.grid_2)
                        ) {
                            item {
                                Row (
                                    Modifier
                                        .padding(
                                        bottom = PetClinicAppointmentTheme.dimensions.grid_1
                                    )
                                ){
                                    TableHeader(title = "Layanan", modifier = Modifier.weight(0.25f))
                                    TableHeader(title = "Jadwal Mulai", modifier = Modifier.weight(0.25f))
                                    TableHeader(title = "Jadwal Berakhir", modifier = Modifier.weight(0.25f))
                                    TableHeader(title = "Harga", modifier = Modifier.weight(0.25f))
                                }
                            }
                            items(listOf(appointmentDetail)) {
                                Row {
                                    TableValue(modifier = Modifier.weight(0.25f)){
                                        Text(
                                            "$serviceName",
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                    TableValue(modifier = Modifier.weight(0.25f)){
                                        Column (
                                            Modifier.fillMaxWidth(),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ){
                                            Text(LocalDateTime.parse(serviceStartSchedule).format(
                                                DateTimeFormatter.ofPattern("dd MMM YYYY")))
                                            Text(LocalDateTime.parse(appointmentDetail?.serviceSchedule?.startSchedule).format(DateTimeFormatter.ofPattern("HH:mm")))
                                        }
                                    }
                                    TableValue(modifier = Modifier.weight(0.25f)){
                                        Column (
                                            Modifier.fillMaxWidth(),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ){
                                            Text(LocalDateTime.parse(serviceEndSchedule).format(DateTimeFormatter.ofPattern("dd MMM YYYY")))
                                            Text(LocalDateTime.parse(appointmentDetail?.serviceSchedule?.endSchedule).format(DateTimeFormatter.ofPattern("HH:mm")))
                                        }
                                    }
                                    TableValue(modifier = Modifier.weight(0.25f)){
                                        Text(
                                            DecimalFormat("0.#").format(servicePrice),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                        Divider(Modifier.fillMaxWidth())
                    }
                }
            }
            if(!userRole.isNullOrEmpty() && !appointmentStatus.isNullOrEmpty()) {
                Divider(Modifier.fillMaxWidth())
                AppointmentAction(
                    appointmentDetailViewModel = appointmentDetailViewModel,
                    onClickAppointmentAction = { statusId -> onClickAppointmentAction(statusId) }
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
            style = PetClinicAppointmentTheme.typography.h3,
            textAlign = TextAlign.Center
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
        horizontalArrangement = Arrangement.Center,
    ) {
        content()
    }
}

@Composable
fun AppointmentAction(
    appointmentDetailViewModel: AppointmentDetailViewModel,
    onClickAppointmentAction: (statusId: Int) -> Unit
){
    val appointmentDetail by appointmentDetailViewModel.appointmentDetail.collectAsState()
    val appointmentStatusCode = appointmentDetail?.status
    val appointmentActionList by appointmentDetailViewModel.appointmentActionList.collectAsState()

    LaunchedEffect(appointmentStatusCode){
        appointmentDetailViewModel.getAppointmentActionList()
    }

    Row (
        Modifier.fillMaxWidth()
    ) {
        appointmentActionList.map {
            val statusText = appointmentDetailViewModel.appointmentStatusCodeToStatusActionMap[it]
            if(statusText !== null) {
                AppButton(
                    onClick = { onClickAppointmentAction(it) },
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