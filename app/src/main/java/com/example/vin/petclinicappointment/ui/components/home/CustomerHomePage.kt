package com.example.vin.petclinicappointment.ui.components.home

import android.Manifest
import android.content.Context
import android.content.res.Resources
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.MedicalServices
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import java.util.Locale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.vin.petclinicappointment.R
import com.example.vin.petclinicappointment.data.model.AppointmentDetail
import com.example.vin.petclinicappointment.ui.components.common.*
import com.example.vin.petclinicappointment.ui.theme.Gray
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ResolvableApiException
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomerHomePage(
    homeViewModel: HomeViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState,
    navigateToSearchPetClinic: () -> Unit,
    navigateToAppointmentDetail: (appointmentId: Int) -> Unit,
    getLocation: (context: Context, onFail: () -> Unit) -> Unit,
    getLocationPermissionStatus: (context: Context) -> Boolean,
    getGpsEnabledStatus: (context: Context, onEnabled: () -> Unit, onDisabled: (exception: Exception) -> Unit) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val localFocusManager = LocalFocusManager.current
    val context = LocalContext.current
    val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION


    fun onFailGetLocation() {
        coroutineScope.launch {
            scaffoldState.snackbarHostState.showSnackbar(
                Resources.getSystem().getString(R.string.fail_get_location_message),
                Resources.getSystem().getString(R.string.close))
        }
    }

    val ACCEPTED_CODE_GPS = -1
    val settingLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { activityResult ->
            if (activityResult.resultCode == ACCEPTED_CODE_GPS) {
                getLocation(context) { onFailGetLocation() }
            }
        }

    fun onLocationPermissionGranted() {
        getGpsEnabledStatus(
            context,
            { getLocation(context) { onFailGetLocation() } },
            { exception ->
                if (exception is ResolvableApiException) {
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(exception.resolution).build()
                    settingLauncher.launch(intentSenderRequest)
                }
            }
        )
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
            if (permissionGranted) {
                onLocationPermissionGranted()
            }
        }

    LaunchedEffect(Unit){
        homeViewModel.message.collectLatest {
            if(it.isNotEmpty()) {
                scaffoldState.snackbarHostState.showSnackbar(it)
            }
        }
    }

    LaunchedEffect(Unit) {
//        val googleApiAvailability = GoogleApiAvailability.getInstance()
//        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context)
//        Log.d("debug1", "available:${resultCode == ConnectionResult.SUCCESS}")
        val locationPermissionStatus = getLocationPermissionStatus(context)
        if (!locationPermissionStatus) {
            launcher.launch(locationPermission)
        } else {
            onLocationPermissionGranted()
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    localFocusManager.clearFocus()
                })
            },
        color = MaterialTheme.colors.background
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(PetClinicAppointmentTheme.colors.primary)
        ) {
            HomeHeader(navigateToPetClinicList = navigateToSearchPetClinic)
            Column(
                Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(
                        topStart = PetClinicAppointmentTheme.dimensions.grid_4,
                        topEnd = PetClinicAppointmentTheme.dimensions.grid_4
                    ))
                    .background(PetClinicAppointmentTheme.colors.surface)
            ) {
                Column(
                    Modifier
                        .padding(
                            top = PetClinicAppointmentTheme.dimensions.grid_5
                        )
                ) {
                    TodayApprovedAppointmentList(
                        homeViewModel,
                        navigateToAppointmentDetail
                    )
                }
            }
        }
    }
}

@Composable
fun TodayApprovedAppointmentList(
    homeViewModel: HomeViewModel,
    navigateToAppointmentDetail: (appointmentId: Int) -> Unit
){
    val todayApprovedAppointmentList = homeViewModel.todayApprovedAppointmentList.collectAsLazyPagingItems()
    Column {
        Column {
            Text(
                stringResource(R.string.coming_soon_appointment),
                style = PetClinicAppointmentTheme.typography.h3,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(
                    start = PetClinicAppointmentTheme.dimensions.grid_2,
                    bottom = PetClinicAppointmentTheme.dimensions.grid_0_5
                )
            )
            Divider(Modifier.fillMaxWidth())
        }
        LazyColumn(
            Modifier.fillMaxSize()
        ) {
            items(todayApprovedAppointmentList) {
                if (it != null) {
                    TodayApprovedAppointmentItem(it, navigateToAppointmentDetail)
                }
            }
            todayApprovedAppointmentList.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item { ProgressIndicatorView(modifier = Modifier.fillParentMaxSize()) }
                    }
                    loadState.append is LoadState.Loading -> {
                        item { ProgressIndicatorView() }
                    }
                    (loadState.refresh is LoadState.NotLoading &&
                            todayApprovedAppointmentList.itemCount == 0) -> {
                        item {
                            MessageView(
                                message = stringResource(R.string.no_coming_soon_appointment),
                                modifier = Modifier
                                    .fillParentMaxSize()
                            )
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun TodayApprovedAppointmentItem(
    appointment: AppointmentDetail,
    navigateToAppointmentDetail: (appointmentId: Int) -> Unit
){
    val startSchedule = LocalDateTime.parse(appointment.serviceSchedule.startSchedule)
    val endSchedule = LocalDateTime.parse(appointment.serviceSchedule.endSchedule)
    val startDate = startSchedule.toLocalDate()
    val startTime = startSchedule.format(DateTimeFormatter.ofPattern("HH:mm"))
    val endTime = endSchedule.format(DateTimeFormatter.ofPattern("HH:mm"))

    Column {
        Card(
            Modifier
                .fillMaxWidth()
                .height(PetClinicAppointmentTheme.dimensions.grid_8 * 2)
                .clickable { navigateToAppointmentDetail(appointment.id) },
            elevation = 0.dp
        ) {
            Row(
                Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                View(
                    Modifier
                        .padding(PetClinicAppointmentTheme.dimensions.grid_2)
                        .width(PetClinicAppointmentTheme.dimensions.grid_9)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    if (appointment.petClinic.image !== null) {
                        Image(
                            base64 = appointment.petClinic.image,
                            contentScale = ContentScale.Fit,
                            contentDescription = "clinic image",
                            modifier = Modifier
                                .size(PetClinicAppointmentTheme.dimensions.grid_10)
                                .clip(RoundedCornerShape(10))
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.default_clinic_image),
                            contentScale = ContentScale.Fit,
                            contentDescription = "default clinic image",
                            modifier = Modifier
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(10))
                        )
                    }
                }
                Column(
                    Modifier
                        .padding(PetClinicAppointmentTheme.dimensions.grid_1)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        appointment.petClinic.name ?: "",
                        style = PetClinicAppointmentTheme.typography.h3,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(
                            bottom = PetClinicAppointmentTheme.dimensions.grid_1
                        )
                    )
                    Column {
                        Row(
                            Modifier
                                .padding(bottom = PetClinicAppointmentTheme.dimensions.grid_0_25),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.MedicalServices,
                                contentDescription = "medical service icon",
                                modifier = Modifier
                                    .padding(end = PetClinicAppointmentTheme.dimensions.grid_1)
                                    .size(PetClinicAppointmentTheme.dimensions.grid_2_5),
                                tint = Gray
                            )
                            Text(appointment.service.name,
                                style = PetClinicAppointmentTheme.typography.body1,
                                fontWeight = FontWeight.SemiBold)
                        }
                        Row(
                            Modifier
                                .padding(bottom = PetClinicAppointmentTheme.dimensions.grid_0_25),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.CalendarToday,
                                contentDescription = "calendar icon",
                                modifier = Modifier
                                    .padding(end = PetClinicAppointmentTheme.dimensions.grid_1)
                                    .size(PetClinicAppointmentTheme.dimensions.grid_2_5),
                                tint = Gray
                            )
                            Text("${startDate.dayOfMonth} ${
                                startDate.month.getDisplayName(TextStyle.FULL,
                                    Locale.getDefault())
                            } ${startDate.year}",
                                style = PetClinicAppointmentTheme.typography.body1,
                            fontWeight = FontWeight.SemiBold)
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Schedule,
                                contentDescription = "clock icon",
                                modifier = Modifier
                                    .padding(end = PetClinicAppointmentTheme.dimensions.grid_1)
                                    .size(PetClinicAppointmentTheme.dimensions.grid_2_5),
                                tint = Gray
                            )
                            Text(
                                "$startTime - $endTime",
                                style = PetClinicAppointmentTheme.typography.body1,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
        Divider(Modifier.fillMaxWidth())
    }
}