package com.example.vin.petclinicappointment.ui.components.petclinic

import android.content.Context
import android.content.res.Resources
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.rounded.NavigateBefore
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vin.petclinicappointment.R
import com.example.vin.petclinicappointment.data.model.GeocodingApiResult
import com.example.vin.petclinicappointment.ui.components.common.TextInput
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.vin.petclinicappointment.ui.components.common.IconButton
import com.example.vin.petclinicappointment.ui.components.common.CircularProgressIndicator
import com.example.vin.petclinicappointment.ui.theme.Black_50

@Composable
fun CurrentLocationMapPage(
    scaffoldState: ScaffoldState,
    currentLocationMapViewModel: CurrentLocationMapViewModel = hiltViewModel(),
    selectedLocationState: MutableState<GeocodingApiResult?>,
    deviceLocationState: MutableState<GeocodingApiResult?>,
    getDeviceLocation: (context: Context, onFail: () -> Unit) -> Unit,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val localFocusManager = LocalFocusManager.current
    var progressIndicatorVisible by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val searchLocationValue by currentLocationMapViewModel.searchLocationValue.collectAsState()
    val selectedLocation by currentLocationMapViewModel.selectedLocation.collectAsState()
    val locationRecommendationList by currentLocationMapViewModel.locationRecommendationList.collectAsState()
    var searchJob: Job? = null
    var textFieldDropDownExpanded by rememberSaveable { mutableStateOf(false) }
    var searchLocationTextFieldWidth by rememberSaveable { mutableStateOf(0) }

    fun onFailGetLocation(){
        coroutineScope.launch {
            scaffoldState.snackbarHostState.showSnackbar(
                Resources.getSystem().getString(R.string.fail_get_location_message),
                Resources.getSystem().getString(R.string.close))
        }
    }

    fun onClickNext(){
        selectedLocationState.value = selectedLocation
        navigateBack()
    }

    LaunchedEffect(Unit) {
        progressIndicatorVisible = true
        selectedLocationState.value?.let { selectedLocation ->
            currentLocationMapViewModel.setSearchLocationValue(selectedLocation.formatted)
            currentLocationMapViewModel.setSelectedLocation(selectedLocation)
        }
        progressIndicatorVisible = false
    }
    Surface(
        Modifier.fillMaxSize()
    ) {
        selectedLocation.let {
            if (it != null && !progressIndicatorVisible) {
                Box {
                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(LatLng(it.lat, it.lon), 15f)
                    }
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        onMapClick = { localFocusManager.clearFocus() }
                    ) {
                        Marker(
                            position = LatLng(it.lat, it.lon),
                            title = stringResource(R.string.my_location)
                        )
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(PetClinicAppointmentTheme.dimensions.grid_8)
                            .padding(
                                start = PetClinicAppointmentTheme.dimensions.grid_2,
                                end = PetClinicAppointmentTheme.dimensions.grid_2
                            ),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            TextInput(
                                value = searchLocationValue,
                                onValueChange = { value ->
                                    if(value.isNotEmpty()) {
                                        searchJob?.cancel()
                                        searchJob = coroutineScope.launch(Dispatchers.IO) {
                                            currentLocationMapViewModel.setSearchLocationValue(value)
                                            delay(1000)
                                            currentLocationMapViewModel.getAddressAutocompleteList()
                                            textFieldDropDownExpanded = true
                                        }
                                    } else {
                                        currentLocationMapViewModel.setSearchLocationValue(value)
                                        textFieldDropDownExpanded = true
                                    }
                                },
                                containerModifier = Modifier
                                    .width(PetClinicAppointmentTheme.dimensions.grid_5 * 7)
                                    .onSizeChanged { size ->
                                        searchLocationTextFieldWidth = size.width
                                    },
                                modifier = Modifier
                                    .width(PetClinicAppointmentTheme.dimensions.grid_5 * 7)
                                    .background(Color.White)
                                    .onSizeChanged { size ->
                                        searchLocationTextFieldWidth = size.width
                                    },
                                shape = RoundedCornerShape(10),
                                onFocus = { textFieldDropDownExpanded = true },
                                keyBoardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                keyBoardActions = KeyboardActions(onSearch = {
                                    coroutineScope.launch {
                                        currentLocationMapViewModel.getAddressAutocompleteList()
                                        textFieldDropDownExpanded = true
                                    }
                                })
                            )
                            DropdownMenu(
                                expanded = textFieldDropDownExpanded,
                                onDismissRequest = { textFieldDropDownExpanded = false },
                                properties = PopupProperties(focusable = false),
                                modifier = Modifier.width(with(LocalDensity.current) { searchLocationTextFieldWidth.toDp() })
                            ) {
                            DropdownMenuItem(onClick = {
                                getDeviceLocation(context) { onFailGetLocation() }
                                deviceLocationState.value.let { deviceLocation ->
                                    if (deviceLocation != null) {
                                        currentLocationMapViewModel.setSearchLocationValue(deviceLocation.formatted)
                                        currentLocationMapViewModel.setSelectedLocation(deviceLocation)
                                        cameraPositionState.move(CameraUpdateFactory.newLatLng(
                                            LatLng(deviceLocation.lat, deviceLocation.lon)))
                                    }
                                }
                            }) { CurrentLocationOption() }
                                if (locationRecommendationList.isNotEmpty()) {
                                    locationRecommendationList.forEach { location ->
                                        DropdownMenuItem(onClick = {
                                            currentLocationMapViewModel.setSelectedLocation(location)
                                            currentLocationMapViewModel.setSearchLocationValue(
                                                location.formatted)
                                            cameraPositionState.move(CameraUpdateFactory.newLatLng(
                                                LatLng(location.lat, location.lon)))
                                        }) {
                                            Text(location.formatted)
                                        }
                                    }
                                } else {
                                    DropdownMenuItem(onClick = {}) {
                                        Text(stringResource(R.string.location_not_found))
                                    }
                                }
                            }
                        }
                    }
                }
                Box (
                    contentAlignment = Alignment.BottomEnd
                ){
                    FloatingActionButton(
                        onClick = { onClickNext() },
                        modifier = Modifier.padding(
                            horizontal = PetClinicAppointmentTheme.dimensions.grid_2,
                            vertical = PetClinicAppointmentTheme.dimensions.grid_5 * 3
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.NavigateNext,
                            contentDescription = "set location",
                            tint = PetClinicAppointmentTheme.colors.onPrimary,
                            modifier = Modifier.size(PetClinicAppointmentTheme.dimensions.grid_4)
                        )
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
fun CurrentLocationOption(){
    Row {
        Icon(
            Icons.Default.MyLocation,
            contentDescription = "current location",
            modifier = Modifier.padding(end = PetClinicAppointmentTheme.dimensions.grid_0_5)
        )
        Text(stringResource(R.string.use_current_location))
    }
}



