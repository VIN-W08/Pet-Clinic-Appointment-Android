package com.example.vin.petclinicappointment.ui.components.petclinic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.NavigateBefore
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vin.petclinicappointment.R
import com.example.vin.petclinicappointment.data.model.ClinicSchedule
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import com.example.vin.petclinicappointment.ui.components.common.IconButton
import com.example.vin.petclinicappointment.ui.theme.Black_50
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PetClinicInfoPage(
    petClinicInfoViewModel: PetClinicInfoViewModel = hiltViewModel(),
    id: Int,
    navigateBack: () -> Unit,
    navigateToLocationMap: (name: String, lat: Double, lon: Double) -> Unit,
    scaffoldState: ScaffoldState
){
    var progressIndicatorVisible by rememberSaveable { mutableStateOf(false) }
    val clinicDetail by petClinicInfoViewModel.clinicDetail.collectAsState()
    val clinicVillage by petClinicInfoViewModel.village.collectAsState()
    val clinicDistrict by petClinicInfoViewModel.district.collectAsState()
    val clinicCity by petClinicInfoViewModel.city.collectAsState()
    val clinicProvince by petClinicInfoViewModel.province.collectAsState()
    val clinicName = clinicDetail?.name
    val clinicAddress = clinicDetail?.address
    val clinicPhoneNum = clinicDetail?.phoneNum
    val clinicScheduleList = clinicDetail?.clinicScheduleList
    val clinicLatitude = clinicDetail?.latitude
    val clinicLongitude = clinicDetail?.longitude

    LaunchedEffect(Unit){
        petClinicInfoViewModel.message.collectLatest {
            if(it.isNotEmpty()) {
                scaffoldState.snackbarHostState.showSnackbar(it)
            }
        }
    }

    LaunchedEffect(Unit){
        progressIndicatorVisible = true
        petClinicInfoViewModel.getPetClinicDetail(id)
        petClinicInfoViewModel.getVillageDetail()
        petClinicInfoViewModel.getDistrictDetail()
        petClinicInfoViewModel.getCityDetail()
        petClinicInfoViewModel.getProvinceDetail()
        progressIndicatorVisible = false
    }

    Surface (
        color = PetClinicAppointmentTheme.colors.background
    ) {
        if(!progressIndicatorVisible){
        if(clinicDetail !== null) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .height(PetClinicAppointmentTheme.dimensions.grid_6 * 3)
                ) {
                    if (clinicLatitude !== null && clinicLongitude !== null && clinicName !== null) {
                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            cameraPositionState = rememberCameraPositionState {
                                position = CameraPosition.fromLatLngZoom(LatLng(clinicLatitude,
                                    clinicLongitude), 16f)
                            },
                            onMapClick = {
                                navigateToLocationMap(
                                    clinicName,
                                    clinicLatitude,
                                    clinicLongitude
                                )
                            },
                            uiSettings = MapUiSettings(
                                scrollGesturesEnabled = false,
                                zoomControlsEnabled = false,
                                zoomGesturesEnabled = false
                            )
                        ) {
                            Marker(
                                position = LatLng(clinicLatitude, clinicLongitude),
                                title = clinicName
                            )
                        }
                    }
                }
                Divider(Modifier.fillMaxWidth())
                Row(
                    Modifier
                        .padding(
                            top = PetClinicAppointmentTheme.dimensions.grid_2,
                            bottom = PetClinicAppointmentTheme.dimensions.grid_3,
                            start = PetClinicAppointmentTheme.dimensions.grid_2,
                            end = PetClinicAppointmentTheme.dimensions.grid_2
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        Modifier.fillMaxWidth()
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (clinicName != null) {
                                Text(
                                    clinicName,
                                    style = PetClinicAppointmentTheme.typography.h1,
                                    modifier = Modifier.padding(
                                        bottom = PetClinicAppointmentTheme.dimensions.grid_1
                                    )
                                )
                            }
                        }
                        if (clinicAddress !== null &&
                            clinicVillage !== null &&
                            clinicDistrict !== null &&
                            clinicCity !== null &&
                            clinicProvince !== null
                        ) {
                            Text(
                                "$clinicAddress, ${clinicVillage?.name}, ${clinicDistrict?.name}, ${clinicCity?.name}, ${clinicProvince?.name}",
                                style = PetClinicAppointmentTheme.typography.h3,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.padding(
                                    bottom = PetClinicAppointmentTheme.dimensions.grid_1
                                )
                            )
                        }
                        if (clinicPhoneNum !== null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Call,
                                    contentDescription = "phone",
                                    modifier = Modifier
                                        .padding(end = PetClinicAppointmentTheme.dimensions.grid_1)
                                        .size(PetClinicAppointmentTheme.dimensions.grid_2)
                                )
                                Text(
                                    "$clinicPhoneNum",
                                    style = PetClinicAppointmentTheme.typography.h3,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                    }
                }
                if (clinicScheduleList != null) {
                    ClinicAvailableScheduleList(
                        clinicScheduleList,
                        petClinicInfoViewModel.dayToStringMap
                    )
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
            com.example.vin.petclinicappointment.ui.components.common.CircularProgressIndicator(
                visible = progressIndicatorVisible)
        }
    }
}

@Composable
fun ClinicAvailableScheduleList(
    scheduleList: List<ClinicSchedule>,
    dayToStringMap: Map<Int, String>
){
    Column(
        Modifier.padding(PetClinicAppointmentTheme.dimensions.grid_2)
    ) {
        (1..7).toList().map {
            ClinicAvailableScheduleItem(
                it,
                scheduleList.filter { schedule -> schedule.day == it },
                dayToStringMap
            )
        }
    }
//    LazyColumn(
//        Modifier.padding(PetClinicAppointmentTheme.dimensions.grid_2)
//    ) {
//        items((1..7).toList()){
//            ClinicAvailableScheduleItem(
//                it,
//                scheduleList.filter { schedule -> schedule.day == it },
//                dayToStringMap
//            )
//        }
//    }
}

@Composable
fun ClinicAvailableScheduleItem(
    day: Int,
    scheduleList: List<ClinicSchedule>,
    dayToStringMap: Map<Int, String>
){
    Row(
        Modifier
            .width(PetClinicAppointmentTheme.dimensions.grid_10 * 2)
            .padding(
                bottom = PetClinicAppointmentTheme.dimensions.grid_1_5
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val dayName = dayToStringMap[day]
        if(dayName !== null) {
            Text(
                dayName.replaceFirstChar { it.uppercase() },
                style = PetClinicAppointmentTheme.typography.h3,
                fontWeight = FontWeight.Normal
            )
            if (scheduleList.isNotEmpty()) {
                Column {
                    scheduleList.map {
                        val starTime = it.startTime.slice(0..(it.startTime.length - 4))
                        val endTime = it.endTime.slice(0..(it.startTime.length - 4))
                        Text(
                            "$starTime - $endTime",
                            style = PetClinicAppointmentTheme.typography.h3,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(
                                bottom = PetClinicAppointmentTheme.dimensions.grid_1
                            )
                        )
                    }
                }
            } else {
                Text(
                    stringResource(R.string.close),
                    style = PetClinicAppointmentTheme.typography.h3,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}