package com.example.vin.petclinicappointment.ui.components.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
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
    val pastAppointmentList  = historyViewModel.pastAppointmentList.collectAsLazyPagingItems()
    val currentAppointmentList  = historyViewModel.currentAppointmentList.collectAsLazyPagingItems()

    Surface (
        Modifier.fillMaxSize(),
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
                Column {
                    if(
                        currentAppointmentList.loadState.refresh is LoadState.Loading ||
                        pastAppointmentList.loadState.refresh is LoadState.Loading
                    ){
                        ProgressIndicatorView(Modifier.fillMaxSize())
                    } else if(
                        currentAppointmentList.loadState.refresh is LoadState.NotLoading &&
                        pastAppointmentList.loadState.refresh is LoadState.NotLoading &&
                        currentAppointmentList.itemCount == 0 &&
                        pastAppointmentList.itemCount == 0
                    ){
                        MessageView(
                            message = stringResource(R.string.no_past_appointment),
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }else {
                        LazyColumn {
                            if(
                                currentAppointmentList.loadState.refresh is LoadState.NotLoading &&
                                currentAppointmentList.itemCount > 0
                            ) {
                                item {
                                    AppointmentListTitle(
                                        "Janji Temu Saat Ini",
                                        Modifier
                                            .padding(
                                                start = PetClinicAppointmentTheme.dimensions.grid_2,
                                                bottom = PetClinicAppointmentTheme.dimensions.grid_1
                                            )
                                    )
                                }
                                items(currentAppointmentList) {
                                    if (it != null) {
                                        AppointmentItem(
                                            appointment = it,
                                            navigateToAppointmentDetail = navigateToAppointmentDetail
                                        )
                                    }
                                }
                                item {
                                    Spacer(Modifier.height(PetClinicAppointmentTheme.dimensions.grid_4))
                                }
                            }
                            if(
                                pastAppointmentList.loadState.refresh is LoadState.NotLoading &&
                                pastAppointmentList.itemCount > 0
                            ) {
                                item {
                                    AppointmentListTitle(
                                        "Janji Temu Lalu",
                                        Modifier
                                            .padding(
                                                start = PetClinicAppointmentTheme.dimensions.grid_2,
                                                bottom = PetClinicAppointmentTheme.dimensions.grid_1
                                            )
                                    )
                                }
                                items(pastAppointmentList) {
                                    if (it != null) {
                                        AppointmentItem(
                                            appointment = it,
                                            navigateToAppointmentDetail = navigateToAppointmentDetail
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
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
fun AppointmentListTitle(
    title: String,
    modifier: Modifier
){
    Column {
        Text(
            title,
            style = PetClinicAppointmentTheme.typography.h2,
            modifier = modifier
        )
        Divider(Modifier.fillMaxWidth())
    }
}

@Composable
fun CurrentAppointmentList(
    historyViewModel: HistoryViewModel,
    title: String,
    navigateToAppointmentDetail: (id: Int) -> Unit,
){
    val currentAppointmentList = historyViewModel.currentAppointmentList.collectAsLazyPagingItems()
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
            items(currentAppointmentList) {
                if (it != null) {
                    AppointmentItem(
                        appointment = it,
                        navigateToAppointmentDetail = navigateToAppointmentDetail
                    )
                }
            }
        }
    }
}

@Composable
fun PastAppointmentList(
    historyViewModel: HistoryViewModel,
    title: String,
    navigateToAppointmentDetail: (id: Int) -> Unit
){
    val pastAppointmentList = historyViewModel.pastAppointmentList.collectAsLazyPagingItems()
    Column(
        Modifier.fillMaxWidth()
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
            items(pastAppointmentList) {
                if (it != null) {
                    AppointmentItem(
                        appointment = it,
                        navigateToAppointmentDetail = navigateToAppointmentDetail
                    )
                }
            }
            if(pastAppointmentList.loadState.append is LoadState.Loading){
                item { ProgressIndicatorView() }
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
                        Modifier
                            .fillMaxHeight(0.6f)
                            .weight(1f)
                            .padding(end = PetClinicAppointmentTheme.dimensions.grid_2),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "${appointment.petClinic.name}",
                            style = PetClinicAppointmentTheme.typography.h3,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 2
                        )
                        Text(
                            LocalDateTime.parse(appointment.createdAt)
                                .format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm"))
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