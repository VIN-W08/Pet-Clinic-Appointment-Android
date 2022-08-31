package com.example.vin.petclinicappointment.ui.components.petclinic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vin.petclinicappointment.R
import com.example.vin.petclinicappointment.data.model.GeocodingApiResult
import com.example.vin.petclinicappointment.ui.components.common.TextInput
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import com.example.vin.petclinicappointment.ui.components.common.IconButton
import com.example.vin.petclinicappointment.ui.components.common.MessageView
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SearchPetClinicListPage(
    searchPetClinicListViewModel: SearchPetClinicListViewModel = hiltViewModel(),
    navigateToCurrentLocationMap: () -> Unit,
    navigateBack: () -> Unit,
    navigateToDetail: (id: Int) -> Unit,
    selectedLocationState: StateFlow<GeocodingApiResult?>,
    scaffoldState: ScaffoldState
) {
    val selectedLocationState by selectedLocationState.collectAsState()
    var progressIndicatorVisible by rememberSaveable { mutableStateOf(false) }
    val localFocusManager = LocalFocusManager.current
    val searchPetClinicListInputValue by searchPetClinicListViewModel.searchPetClinicListInputValue.collectAsState()
    val selectedLocation = selectedLocationState
    val selectedCoordinate by searchPetClinicListViewModel.selectedCoordinate.collectAsState()

    LaunchedEffect(Unit){
        searchPetClinicListViewModel.message.collectLatest {
            if(it.isNotEmpty()) {
                scaffoldState.snackbarHostState.showSnackbar(it)
            }
        }
    }

    LaunchedEffect(selectedLocation){
        searchPetClinicListViewModel.setSelectedCoordinate(
            selectedLocation?.let {
                LatLng(it.lat, it.lon)
            }
        )
    }

    Surface (
        Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = {
                localFocusManager.clearFocus()
            })
        },
        color = PetClinicAppointmentTheme.colors.background
            ) {
        if(!progressIndicatorVisible) {
            Column {
                Row(
                    Modifier
                        .padding(
                            start = PetClinicAppointmentTheme.dimensions.grid_2,
                            end = PetClinicAppointmentTheme.dimensions.grid_2,
                            top = PetClinicAppointmentTheme.dimensions.grid_2,
                        )
                        .height(PetClinicAppointmentTheme.dimensions.grid_7),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        icon = Icons.Default.ArrowBackIos,
                        contentDescription = "arrow_back"
                    ) { navigateBack() }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { navigateToCurrentLocationMap() },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "location on icon",
                            tint = Color.Red,
                            modifier = Modifier
                                .padding(end = PetClinicAppointmentTheme.dimensions.grid_0_5)
                                .size(PetClinicAppointmentTheme.dimensions.grid_2_5)
                        )
                        if(selectedLocation !== null){
                            Text(
                                selectedLocation.formatted,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = PetClinicAppointmentTheme.typography.h3
                            )
                        }else{
                            Text(
                                "Pilih Lokasi",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = PetClinicAppointmentTheme.typography.h3
                            )
                        }
                    }
                }
                TextInput(
                    modifier = Modifier
                        .padding(
                            start = PetClinicAppointmentTheme.dimensions.grid_2,
                            end = PetClinicAppointmentTheme.dimensions.grid_2,
                            top = 0.dp,
                            bottom = PetClinicAppointmentTheme.dimensions.grid_2
                        )
                        .fillMaxWidth(),
                    containerModifier = Modifier.fillMaxWidth(),
                    value = searchPetClinicListInputValue,
                    onValueChange = { value ->
                        searchPetClinicListViewModel.setSearchPetClinicListInputValue(value)
                    },
                    placeholder = stringResource(R.string.search),
                    shape = RoundedCornerShape(30),
                    leadingIcon = {
                        IconButton(
                            icon = Icons.Default.Search,
                            contentDescription = "search"
                        ) {
                        }
                    },
                    keyBoardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                )
                if(selectedCoordinate?.latitude !== null && selectedCoordinate?.longitude !== null) {
                    PetClinicList(
                        searchPetClinicListViewModel = searchPetClinicListViewModel,
                        navigateToDetail
                    )
                }else{
                    MessageView(
                        message = stringResource(R.string.please_set_location),
                        modifier = Modifier.fillMaxSize()
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