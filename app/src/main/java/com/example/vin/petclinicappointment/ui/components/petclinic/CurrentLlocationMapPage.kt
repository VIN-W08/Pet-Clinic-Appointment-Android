package com.example.vin.petclinicappointment.ui.components.petclinic

import android.content.Context
import android.content.res.Resources
import android.util.Log
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
import androidx.compose.material.icons.rounded.AddLocation
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
import kotlinx.coroutines.flow.StateFlow

@Composable
fun CurrentLocationMapPage(
    scaffoldState: ScaffoldState,
    currentLocationMapViewModel: CurrentLocationMapViewModel = hiltViewModel(),
    selectedLocationState: StateFlow<GeocodingApiResult?>,
    setSelectedLocation: (location: GeocodingApiResult?) -> Unit,
    deviceLocationState: MutableState<GeocodingApiResult?>,
    getDeviceLocation: (context: Context, onFail: () -> Unit) -> Unit,
    navigateBack: () -> Unit,
    navigateToSearchPetClinic: () -> Unit
) {
    val selectedLocationState by selectedLocationState.collectAsState()
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
    val selectedLat = selectedLocation?.lat
    val selectedLon = selectedLocation?.lon
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(selectedLat ?: 0.0, selectedLon ?: 0.0),
            15f
        )
    }

    fun onFailGetLocation(){
        coroutineScope.launch {
            scaffoldState.snackbarHostState.showSnackbar(
                Resources.getSystem().getString(R.string.fail_get_location_message),
                Resources.getSystem().getString(R.string.close))
        }
    }

    fun onClickNext(){
        setSelectedLocation(selectedLocation)
        navigateToSearchPetClinic()
    }

    LaunchedEffect(Unit) {
        progressIndicatorVisible = true
        selectedLocationState.let { selectedLocation ->
            if(selectedLocation !== null) {
                currentLocationMapViewModel.setSearchLocationValue(selectedLocation.formatted)
                currentLocationMapViewModel.setSelectedLocation(selectedLocation)
            }
        }
        progressIndicatorVisible = false
    }

    LaunchedEffect(selectedLocation){
        selectedLocation.let {
            if (it !== null &&
                cameraPositionState.position.target == LatLng(0.0, 0.0)
            ) {
                cameraPositionState.move(
                    CameraUpdateFactory.newLatLng(LatLng(it.lat, it.lon))
                )
            }
        }
    }

    Surface(
        Modifier.fillMaxSize()
    ) {
        selectedLocation.let {
            Box {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    onMapClick = { localFocusManager.clearFocus() }
                ) {
                    if (it !== null) {
                        Marker(
                            position = LatLng(it.lat, it.lon),
                            title = stringResource(R.string.my_location)
                        )
                    }
                }
                if(it == null){
                    Column(
                        Modifier
                            .fillMaxSize()
                            .background(PetClinicAppointmentTheme.colors.background),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AddLocation,
                            contentDescription = "add location icon",
                            tint = PetClinicAppointmentTheme.colors.primaryVariant,
                            modifier = Modifier.size(PetClinicAppointmentTheme.dimensions.grid_9_5)
                        )
                        Text(
                            "Tambah Lokasi",
                            style = PetClinicAppointmentTheme.typography.h1
                        )
                    }
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(PetClinicAppointmentTheme.dimensions.grid_8)
                        .padding(
                            start = PetClinicAppointmentTheme.dimensions.grid_2,
                            end = PetClinicAppointmentTheme.dimensions.grid_2
                        ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        icon = Icons.Rounded.NavigateBefore,
                        contentDescription = "arrow_back",
                        hasBorder = true,
                        onClick = { navigateBack() },
                        containerModifier = Modifier
                            .padding(
                                end = PetClinicAppointmentTheme.dimensions.grid_2
                            )
                            .background(Black_50, CircleShape),
                        modifier = Modifier.padding(end = PetClinicAppointmentTheme.dimensions.grid_0_25),
                        tint = Color.White
                    )
                    Column(
                        Modifier.weight(1f)
                    ) {
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
                            placeholder = "Cari Lokasi",
                            containerModifier = Modifier
                                .fillMaxWidth()
                                .onSizeChanged { size ->
                                    searchLocationTextFieldWidth = size.width
                                },
                            modifier = Modifier
                                .fillMaxWidth()
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
                            if(selectedLocation !== null) {
                                DropdownMenuItem(onClick = {
                                    getDeviceLocation(context) { onFailGetLocation() }
                                    deviceLocationState.value.let { deviceLocation ->
                                        if (deviceLocation != null) {
                                            currentLocationMapViewModel.setSearchLocationValue(
                                                deviceLocation.formatted)
                                            currentLocationMapViewModel.setSelectedLocation(
                                                deviceLocation)
                                            cameraPositionState.move(CameraUpdateFactory.newLatLng(
                                                LatLng(deviceLocation.lat, deviceLocation.lon)))
                                        }
                                    }
                                }) { CurrentLocationOption() }
                            }
                            if (locationRecommendationList.isNotEmpty()) {
                                locationRecommendationList.forEach { location ->
                                    DropdownMenuItem(onClick = {
                                        Log.d("debug1", "selected loc: (${location.lat},${location.lon})")
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



