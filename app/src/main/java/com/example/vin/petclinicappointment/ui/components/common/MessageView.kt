package com.example.vin.petclinicappointment.ui.components.common

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme

@Composable
fun MessageView(
    message: String,
    modifier: Modifier = Modifier
){
    Box (
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            message,
            style = PetClinicAppointmentTheme.typography.h3,
            fontWeight = FontWeight.Normal
        )
    }
}