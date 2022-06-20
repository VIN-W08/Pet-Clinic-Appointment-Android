package com.example.vin.petclinicappointment.ui.components.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.vin.petclinicappointment.ui.components.common.AppButton
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.RectangleShape

@Composable
fun LoginOptionsPage(
    navigateToLogin: () -> Unit,
    userRole: MutableState<String?>
){
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
                Text(
                    "Selamat datang",
                    style = PetClinicAppointmentTheme.typography.h3
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "di ",
                        style = PetClinicAppointmentTheme.typography.h3,

                    )
                    Text(
                        "PetClinic",
                        style = PetClinicAppointmentTheme.typography.h2
                    )
                }
            }
            Column (
                Modifier
                    .fillMaxWidth()
                    .padding(
                        start = PetClinicAppointmentTheme.dimensions.grid_2,
                        end = PetClinicAppointmentTheme.dimensions.grid_2,
                        bottom = PetClinicAppointmentTheme.dimensions.grid_2
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
                    ){
                Text(
                    "Peduli dan lindungi kesehatan hewan dengan perawatan terbaik.",
                    color = Color.Gray
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
                        .width(PetClinicAppointmentTheme.dimensions.grid_10 * 4)
                        .height(PetClinicAppointmentTheme.dimensions.grid_7),
                    onClick = { onClickLogin("customer") },
                    colors = buttonColors(PetClinicAppointmentTheme.colors.primary),
                    shape = RoundedCornerShape(PetClinicAppointmentTheme.dimensions.grid_5)
                ) {
                    Text("Masuk sebagai pemilik Hewan ")
                }
                AppButton(
                    modifier = Modifier
                        .padding(bottom = PetClinicAppointmentTheme.dimensions.grid_2)
                        .width(PetClinicAppointmentTheme.dimensions.grid_10 * 4)
                        .height(PetClinicAppointmentTheme.dimensions.grid_7),
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