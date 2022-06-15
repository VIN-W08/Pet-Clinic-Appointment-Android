package com.example.vin.petclinicappointment.ui.components.service

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vin.petclinicappointment.data.model.Service
import com.example.vin.petclinicappointment.ui.components.common.AppButton
import com.example.vin.petclinicappointment.ui.components.common.CircularProgressIndicator
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import java.text.DecimalFormat

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ServicePage(
    serviceViewModel: ServiceViewModel = hiltViewModel(),
    navigateToServiceDetail: (id: Int) -> Unit,
    navigateToAddService: () -> Unit
) {
    val serviceList by serviceViewModel.serviceList.collectAsState()
    var progressIndicatorVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        progressIndicatorVisible = true
        serviceViewModel.getServiceList()
        progressIndicatorVisible = false
    }

    Surface(
        color = MaterialTheme.colors.background
    ) {
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
            Column {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    AppButton(
                        onClick = { navigateToAddService() },
                        colors = buttonColors(PetClinicAppointmentTheme.colors.secondary),
                        shape = RoundedCornerShape(PetClinicAppointmentTheme.dimensions.grid_2),
                        modifier = Modifier
                            .padding(
                                end = PetClinicAppointmentTheme.dimensions.grid_2,
                                bottom = PetClinicAppointmentTheme.dimensions.grid_2
                            )
                    ) {
                        Row(
                            modifier = Modifier.width(PetClinicAppointmentTheme.dimensions.grid_10),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.AddCircle,
                                contentDescription = "add_circle"
                            )
                            Text("Layanan")
                        }
                    }
                }
                Column(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    if (serviceList.isEmpty() && !progressIndicatorVisible) {
                        NoServiceView(Modifier.fillMaxSize())
                    }else{
                        Divider(Modifier.fillMaxWidth())
                        ServiceList(
                            serviceList = serviceList,
                            navigateToServiceDetail = navigateToServiceDetail
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
    }
}

@Composable
fun ServiceList(
    serviceList: List<Service>,
    navigateToServiceDetail: (id: Int) -> Unit,
    modifier: Modifier = Modifier,
){
    LazyColumn(
        modifier = modifier
    ){
        items(serviceList) { service ->
            ServiceItem(
                service = service,
                navigateToServiceDetail = navigateToServiceDetail
            )
        }
    }
}

@Composable
fun ServiceItem(
    service: Service,
    navigateToServiceDetail: (id: Int) -> Unit
){
    Column {
        Row(
            Modifier
                .clickable { navigateToServiceDetail(service.id) }
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