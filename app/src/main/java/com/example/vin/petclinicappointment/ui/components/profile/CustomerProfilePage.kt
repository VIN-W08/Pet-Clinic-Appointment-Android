package com.example.vin.petclinicappointment.ui.components.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vin.petclinicappointment.ui.components.common.AppButton
import com.example.vin.petclinicappointment.ui.components.common.CircularProgressIndicator
import com.example.vin.petclinicappointment.ui.components.common.PrefTextView
import com.example.vin.petclinicappointment.ui.theme.Gray
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun CustomerProfilePage(
    customerProfileViewModel: CustomerProfileViewModel = hiltViewModel(),
    navigateToEditCustomerProfile: () -> Unit,
    navigateToLoginOption: () -> Unit,
    scaffoldState: ScaffoldState
){
    val coroutineScope = rememberCoroutineScope()
    var progressIndicatorVisible by rememberSaveable { mutableStateOf(false) }
    val name by customerProfileViewModel.name.collectAsState()
    val email by customerProfileViewModel.email.collectAsState()

    LaunchedEffect(Unit){
        customerProfileViewModel.message.collectLatest {
            if(it.isNotEmpty()) {
                scaffoldState.snackbarHostState.showSnackbar(it)
            }
        }
    }

    LaunchedEffect(Unit){
        progressIndicatorVisible = true
        customerProfileViewModel.getCustomerDetail()
        progressIndicatorVisible = false
    }

    fun checkDataReady(): Boolean {
        return name.trim().isNotEmpty() && email.trim().isNotEmpty()
    }

    fun logout(){
        coroutineScope.launch {
            progressIndicatorVisible = true
            customerProfileViewModel.logout()
            progressIndicatorVisible = false
            navigateToLoginOption()
        }
    }

    Surface(
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
                    "Profil",
                    style = PetClinicAppointmentTheme.typography.h1
                )
            }
            if(checkDataReady()) {
                Column(
                    Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    Column(
                        Modifier.padding(
                            bottom = PetClinicAppointmentTheme.dimensions.grid_4
                        )
                    ) {
                        Column {
                            Text(
                                "Data Pribadi",
                                style = PetClinicAppointmentTheme.typography.h3,
                                modifier = Modifier.padding(
                                    start = PetClinicAppointmentTheme.dimensions.grid_2,
                                    top = PetClinicAppointmentTheme.dimensions.grid_2,
                                    bottom = PetClinicAppointmentTheme.dimensions.grid_0_5
                                ),
                                color = Gray
                            )
                            Divider(
                                Modifier.fillMaxWidth()
                            )
                        }
                        PrefTextView(title = "Nama", value = name)
                        PrefTextView(title = "Email", value = email)
                    }
                }
                Column {
                    AppButton(
                        onClick = navigateToEditCustomerProfile,
                        modifier = Modifier
                            .padding(
                                start = PetClinicAppointmentTheme.dimensions.grid_2,
                                end = PetClinicAppointmentTheme.dimensions.grid_2,
                                bottom = PetClinicAppointmentTheme.dimensions.grid_2
                            )
                            .fillMaxWidth()
                            .height(PetClinicAppointmentTheme.dimensions.grid_5_5),
                        colors = ButtonDefaults.buttonColors(PetClinicAppointmentTheme.colors.secondary),
                        shape = RoundedCornerShape(PetClinicAppointmentTheme.dimensions.grid_5)
                    ) {
                        Text("Ubah Profil")
                    }
                    AppButton(
                        onClick = { logout() },
                        modifier = Modifier
                            .padding(
                                start = PetClinicAppointmentTheme.dimensions.grid_2,
                                end = PetClinicAppointmentTheme.dimensions.grid_2,
                                bottom = PetClinicAppointmentTheme.dimensions.grid_2
                            )
                            .fillMaxWidth()
                            .height(PetClinicAppointmentTheme.dimensions.grid_5_5),
                        colors = ButtonDefaults.buttonColors(PetClinicAppointmentTheme.colors.error),
                        shape = RoundedCornerShape(PetClinicAppointmentTheme.dimensions.grid_5)
                    ) {
                        Text(
                            "Keluar",
                            color = PetClinicAppointmentTheme.colors.onError
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