package com.example.vin.petclinicappointment.ui.components.home

import android.Manifest
import android.content.Context
import android.content.res.Resources
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.vin.petclinicappointment.R
import com.example.vin.petclinicappointment.data.actionCategoryList
import com.example.vin.petclinicappointment.ui.components.common.ModalBottomSheet
import com.example.vin.petclinicappointment.ui.components.common.View
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import com.google.android.gms.common.api.ResolvableApiException
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomerHomePage(
    scaffoldState: ScaffoldState,
    navigateToSearchPetClinic: () -> Unit,
    navigateTo: (route: String) -> Unit,
    getLocation: (context: Context, onFail: () -> Unit) -> Unit,
    getLocationPermissionStatus: (context: Context) -> Boolean,
    getGpsEnabledStatus: (context: Context, onEnabled: () -> Unit, onDisabled: (exception: Exception) -> Unit) -> Unit,
) {
    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { state -> state != ModalBottomSheetValue.HalfExpanded }
    )
    val selectedActionCategoryList = rememberSaveable { mutableStateOf("") }
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

    LaunchedEffect(Unit) {
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
                    coroutineScope.launch {
                        if (modalBottomSheetState.isVisible) {
                            modalBottomSheetState.hide()
                        }
                    }
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
            ModalBottomSheet(
                Modifier.fillMaxSize(),
                modalBottomSheetState = modalBottomSheetState,
                sheetContent = { MoreCategoryContent(selectedActionCategoryList.value) }
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(
                            topStart = 30.dp,
                            topEnd = 30.dp
                        ))
                        .background(Color.White)
                ) {
                    Column(
                        Modifier.padding(
                            top = PetClinicAppointmentTheme.dimensions.grid_3
                        )
                    ) {
                        ActionCategoryList(
                            modalBottomSheetState,
                            selectedActionCategoryList,
                            "Klinik",
                            actionCategoryList,
                            navigateTo,
                            "pet-clinic-list"
                        )
                        ActionCategoryList(
                            modalBottomSheetState,
                            selectedActionCategoryList,
                            "Layanan",
                            actionCategoryList,
                            navigateTo,
                            "pet-clinic-list"
                        )
                    }
                }
            }
        }
    }
}