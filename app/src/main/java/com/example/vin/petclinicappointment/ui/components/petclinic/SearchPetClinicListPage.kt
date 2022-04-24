package com.example.vin.petclinicappointment.ui.components.petclinic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
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

@Composable
fun SearchPetClinicListPage(
    searchPetClinicListViewModel: SearchPetClinicListViewModel = hiltViewModel(),
    navigateToCurrentLocationMap: () -> Unit,
    navigateBack: () -> Unit,
    selectedLocationState: MutableState<GeocodingApiResult?>,
) {
    val localFocusManager = LocalFocusManager.current
    val searchPetClinicListInputValue by searchPetClinicListViewModel.searchPetClinicListInputValue.collectAsState()
    val nearbyPetClinicList by searchPetClinicListViewModel.nearbyPetClinicList.collectAsState()

    Surface (
        Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = {
                localFocusManager.clearFocus()
            })
        },
        color = PetClinicAppointmentTheme.colors.background
            ) {
        Column {
            Row (
                Modifier
                    .padding(
                        start = PetClinicAppointmentTheme.dimensions.grid_2,
                        end = PetClinicAppointmentTheme.dimensions.grid_2,
                        top = PetClinicAppointmentTheme.dimensions.grid_2,
                    )
                    .fillMaxHeight(0.07f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    icon = Icons.Default.ArrowBackIos,
                    contentDescription = "arrow_back"
                ) { navigateBack() }
                selectedLocationState.value?.let {
                    Row(
                        Modifier.clickable { navigateToCurrentLocationMap() },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "location on",
                            tint = Color.Red,
                            modifier = Modifier
                                .padding(end = PetClinicAppointmentTheme.dimensions.grid_0_5)
                                .size(PetClinicAppointmentTheme.dimensions.grid_2_5)
                        )
                        Text(
                            it.formatted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = PetClinicAppointmentTheme.typography.h3
                        )
                    }
                }
            }
            TextInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = PetClinicAppointmentTheme.dimensions.grid_2,
                        end = PetClinicAppointmentTheme.dimensions.grid_2,
                        top = 0.dp,
                        bottom = PetClinicAppointmentTheme.dimensions.grid_2
                    ),
                value = searchPetClinicListInputValue,
                onValueChange = { value -> searchPetClinicListViewModel.setSearchPetClinicListInputValue(value) },
                placeholder = stringResource(R.string.search),
                shape = RoundedCornerShape(30),
                leadingIcon = {
                    IconButton(
                        icon = Icons.Default.Search,
                        contentDescription = "search"
                    ){  searchPetClinicListViewModel.getNearbyPetClinicList() }
                },
                keyBoardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyBoardActions = KeyboardActions(
                    onSearch = {
                        searchPetClinicListViewModel.getNearbyPetClinicList()
                            localFocusManager.clearFocus()
                        }),
            )
            PetClinicList(nearbyPetClinicList)
        }
    }
}