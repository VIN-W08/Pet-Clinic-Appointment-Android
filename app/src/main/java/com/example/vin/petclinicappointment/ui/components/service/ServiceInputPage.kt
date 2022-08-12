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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vin.petclinicappointment.ui.components.common.*
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

@Composable
fun ServiceInputPage(
    serviceInputViewModel: ServiceInputViewModel = hiltViewModel(),
    serviceId: Int? = null,
    pageType: String,
    navigateBack: () -> Unit,
    navigateToService: (() -> Unit)? = null,
    navigateToServiceDetail: ((serviceId: Int) -> Unit)? = null,
    scaffoldState: ScaffoldState
){
    val localFocusManager = LocalFocusManager.current
    val serviceName by serviceInputViewModel.serviceName.collectAsState()
    val servicePrice by serviceInputViewModel.servicePrice.collectAsState()
    val serviceStatus by serviceInputViewModel.serviceStatus.collectAsState()
    var progressIndicatorVisible by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    fun onClickAddService(){
        coroutineScope.launch {
            progressIndicatorVisible = true
            val isSuccess = serviceInputViewModel.addService()
            progressIndicatorVisible = false
            if(isSuccess) {
                if (navigateToService != null) {
                    navigateToService()
                }
            }
        }
    }

    fun onClickUpdateService(){
        coroutineScope.launch {
            progressIndicatorVisible = true
            val isSuccess = serviceInputViewModel.updateService()
            progressIndicatorVisible = false
            if(isSuccess) {
                if (navigateToServiceDetail != null && serviceId != null) {
                    navigateToServiceDetail(serviceId)
                }
            }
        }
    }

    fun onClickDeleteService(){
        coroutineScope.launch {
            progressIndicatorVisible = true
            val isSuccess = serviceInputViewModel.deleteService()
            progressIndicatorVisible = false
            if(isSuccess) {
                if (navigateToService !== null) {
                    navigateToService()
                }
            }
        }
    }

    LaunchedEffect(Unit){
        serviceInputViewModel.message.collectLatest {
            if(it.isNotEmpty()) {
                scaffoldState.snackbarHostState.showSnackbar(it)
            }
        }
    }

    LaunchedEffect(Unit){
        if(serviceId!==null && pageType == "update") {
            serviceInputViewModel.setServiceId(serviceId)
            serviceInputViewModel.getServiceDetail()
            serviceInputViewModel.populateServiceData()
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
                        "add" -> "Tambah Layanan"
                        "update" -> "Ubah Layanan"
                        else -> throw IllegalArgumentException()
                    },
                    style = PetClinicAppointmentTheme.typography.h1
                )
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                LabelTextInput(
                    label = "Nama Layanan",
                    value = serviceName,
                    onValueChange = { serviceInputViewModel.setServiceName(it) },
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
                    label = "Harga Layanan",
                    value = servicePrice,
                    onValueChange = { serviceInputViewModel.setServicePrice(it) },
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
                        label = "Status Aktivasi",
                        value = serviceStatus,
                        onSwitchChange = { serviceInputViewModel.setServiceStatus(it) },
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
                        onClick = { onClickDeleteService() },
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
                            "Hapus Layanan",
                            color = PetClinicAppointmentTheme.colors.onError
                        )
                    }
                }
                AppButton(
                    onClick = {
                              when(pageType){
                                  "add" -> onClickAddService()
                                  "update" -> onClickUpdateService()
                                  else -> throw IllegalArgumentException()
                              }
                    },
                    modifier = Modifier
                        .padding(
                            start = PetClinicAppointmentTheme.dimensions.grid_2,
                            end = PetClinicAppointmentTheme.dimensions.grid_2,
                            bottom = PetClinicAppointmentTheme.dimensions.grid_2,
                            top = if(pageType == "add") PetClinicAppointmentTheme.dimensions.grid_2 else 0.dp
                        )
                        .fillMaxWidth()
                        .height(PetClinicAppointmentTheme.dimensions.grid_5_5),
                    colors = ButtonDefaults.buttonColors(PetClinicAppointmentTheme.colors.primary),
                    shape = RoundedCornerShape(PetClinicAppointmentTheme.dimensions.grid_5)
                ) {
                    Text(
                        when(pageType){
                            "add" -> "Tambah Layanan"
                            "update" -> "Ubah Layanan"
                            else -> throw IllegalArgumentException()
                        }
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