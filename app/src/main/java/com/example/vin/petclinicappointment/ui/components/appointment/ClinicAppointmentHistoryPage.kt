package com.example.vin.petclinicappointment.ui.components.appointment

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.vin.petclinicappointment.R
import com.example.vin.petclinicappointment.ui.components.common.IconButton
import com.example.vin.petclinicappointment.ui.components.common.MessageView
import com.example.vin.petclinicappointment.ui.components.common.ProgressIndicatorView
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ClinicAppointmentHistoryPage(
    clinicAppointmentHistoryViewModel: ClinicAppointmentHistoryViewModel = hiltViewModel(),
    navigateToAppointmentDetail: (id: Int) -> Unit,
    navigateBack: () -> Unit,
    scaffoldState: ScaffoldState
){
    val pastAppointmentList = clinicAppointmentHistoryViewModel.pastAppointmentList.collectAsLazyPagingItems()

    LaunchedEffect(Unit){
        clinicAppointmentHistoryViewModel.message.collectLatest {
            if(it.isNotEmpty()) {
                scaffoldState.snackbarHostState.showSnackbar(it)
            }
        }
    }

    Surface(
        color = MaterialTheme.colors.background
    ) {
        Column {
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
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                pastAppointmentList.apply {
                    AppointmentList(
                        title = stringResource(R.string.past_appointment),
                        appointmentList = pastAppointmentList,
                        navigateToAppointmentDetail = navigateToAppointmentDetail
                    )
                    when {
                        loadState.refresh == LoadState.Loading -> {
                            ProgressIndicatorView(Modifier.fillMaxSize())
                        }
                        loadState.append == LoadState.Loading -> {
                            ProgressIndicatorView(Modifier.fillMaxWidth())
                        }
                        (loadState.refresh is LoadState.NotLoading &&
                                pastAppointmentList.itemCount == 0) -> {
                            MessageView(
                                message = stringResource(R.string.no_past_appointment),
                                modifier = Modifier.fillMaxSize()
                            )
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



