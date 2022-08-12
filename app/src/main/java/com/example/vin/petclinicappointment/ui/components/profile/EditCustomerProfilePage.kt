package com.example.vin.petclinicappointment.ui.components.profile

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vin.petclinicappointment.ui.components.common.AppButton
import com.example.vin.petclinicappointment.ui.components.common.CircularProgressIndicator
import com.example.vin.petclinicappointment.ui.components.common.IconButton
import com.example.vin.petclinicappointment.ui.components.common.LabelTextInput
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun EditCustomerProfilePage(
    editCustomerProfilePageViewModel: EditCustomerProfilePageViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToCustomerProfile: () -> Unit,
    scaffoldState: ScaffoldState
){
    val localFocusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    var progressIndicatorVisible by rememberSaveable { mutableStateOf(false) }
    val name by editCustomerProfilePageViewModel.name.collectAsState()
    val email by editCustomerProfilePageViewModel.email.collectAsState()

    fun onUpdateCustomer(){
        coroutineScope.launch {
            progressIndicatorVisible = true
            val isSuccess = editCustomerProfilePageViewModel.updateCustomer()
            progressIndicatorVisible = false
            if(isSuccess){
                editCustomerProfilePageViewModel.updateLocalUser()
                navigateToCustomerProfile()
            }
        }
    }

    LaunchedEffect(Unit){
        editCustomerProfilePageViewModel.message.collectLatest {
            if(it.isNotEmpty()) {
                scaffoldState.snackbarHostState.showSnackbar(it)
            }
        }
    }

    LaunchedEffect(Unit){
        progressIndicatorVisible = true
        editCustomerProfilePageViewModel.getCustomerDetail()
        progressIndicatorVisible = false
    }

    Surface(
        Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = {
                localFocusManager.clearFocus()
            })
        },
        color = PetClinicAppointmentTheme.colors.background
    ) {
        Column (
            Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                    "Ubah Profil",
                    style = PetClinicAppointmentTheme.typography.h1
                )
            }
            Column(
                Modifier
                    .padding(
                        horizontal = PetClinicAppointmentTheme.dimensions.grid_2
                    )
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                LabelTextInput(
                    label = "Nama",
                    value = name,
                    onValueChange = { editCustomerProfilePageViewModel.setName(it) },
                    modifier = Modifier.padding(bottom = PetClinicAppointmentTheme.dimensions.grid_2)
                )
                LabelTextInput(
                    label = "Email",
                    value = email,
                    onValueChange = { editCustomerProfilePageViewModel.setEmail(it) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    modifier = Modifier.padding(bottom = PetClinicAppointmentTheme.dimensions.grid_2)
                )
            }
            Column {
                Divider(Modifier.fillMaxWidth())
                AppButton(
                    onClick = { onUpdateCustomer() },
                    modifier = Modifier
                        .padding(PetClinicAppointmentTheme.dimensions.grid_2)
                        .fillMaxWidth()
                        .height(PetClinicAppointmentTheme.dimensions.grid_5_5),
                    colors = ButtonDefaults.buttonColors(PetClinicAppointmentTheme.colors.primary),
                    shape = RoundedCornerShape(PetClinicAppointmentTheme.dimensions.grid_5)
                ) {
                    Text("Ubah Profil")
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