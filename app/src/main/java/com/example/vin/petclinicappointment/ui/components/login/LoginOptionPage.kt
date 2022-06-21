package com.example.vin.petclinicappointment.ui.components.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.vin.petclinicappointment.ui.components.common.AppButton
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter

@Composable
fun LoginOptionsPage(
    navigateToLogin: () -> Unit,
    userRole: MutableState<String?>
){
    val context = LocalContext.current
    val appInfo = context.applicationInfo
    val packageManager = context.packageManager
    val launcherIcon = appInfo.loadIcon(packageManager)

    fun onClickLogin(role: String){
        userRole.value = role
        navigateToLogin()
    }

    Surface(
        Modifier
            .fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(
                        top = PetClinicAppointmentTheme.dimensions.grid_2,
                        start = PetClinicAppointmentTheme.dimensions.grid_2,
                        end = PetClinicAppointmentTheme.dimensions.grid_2,
                        bottom = PetClinicAppointmentTheme.dimensions.grid_1
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = rememberAsyncImagePainter(launcherIcon),
                    contentDescription ="launcher icon",
                    modifier = Modifier
                        .padding(bottom = PetClinicAppointmentTheme.dimensions.grid_2)
                        .size(PetClinicAppointmentTheme.dimensions.grid_10 * 2)
                )
                Text(
                    "Selamat datang",
                    style = PetClinicAppointmentTheme.typography.h1,
                    modifier = Modifier.padding(bottom = PetClinicAppointmentTheme.dimensions.grid_2)
                )
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(PetClinicAppointmentTheme.dimensions.grid_2),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                AppButton(
                    modifier = Modifier
                        .padding(bottom = PetClinicAppointmentTheme.dimensions.grid_2)
                        .fillMaxWidth()
                        .height(PetClinicAppointmentTheme.dimensions.grid_6),
                    onClick = { onClickLogin("customer") },
                    colors = buttonColors(PetClinicAppointmentTheme.colors.primary),
                    shape = RoundedCornerShape(PetClinicAppointmentTheme.dimensions.grid_5)
                ) {
                    Text("Masuk sebagai pemilik Hewan ")
                }
                AppButton(
                    modifier = Modifier
                        .padding(bottom = PetClinicAppointmentTheme.dimensions.grid_2)
                        .fillMaxWidth()
                        .height(PetClinicAppointmentTheme.dimensions.grid_6),
                    onClick = { onClickLogin("clinic") },
                    colors = buttonColors(PetClinicAppointmentTheme.colors.primaryVariant),
                    shape = RoundedCornerShape(PetClinicAppointmentTheme.dimensions.grid_5)
                ) {
                    Text("Masuk sebagai pemilik Klinik ")
                }
            }
        }
    }
}