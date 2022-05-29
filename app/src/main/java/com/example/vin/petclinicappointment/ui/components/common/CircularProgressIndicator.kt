package com.example.vin.petclinicappointment.ui.components.common

import androidx.compose.runtime.Composable
import androidx.compose.material.CircularProgressIndicator
import com.example.vin.petclinicappointment.ui.theme.PearlAqua
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import java.lang.reflect.Modifier

@Composable
fun  CircularProgressIndicator(visible: Boolean){

    if(visible) {
        CircularProgressIndicator(
            color = PetClinicAppointmentTheme.colors.secondary,
            strokeWidth = PetClinicAppointmentTheme.dimensions.grid_1
        )
    }
}