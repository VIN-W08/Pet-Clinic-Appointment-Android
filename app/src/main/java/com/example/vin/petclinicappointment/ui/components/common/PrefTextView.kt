package com.example.vin.petclinicappointment.ui.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme

@Composable
fun PrefTextView(
    title: String,
    value: String,
    modifier: Modifier = Modifier
){
    Column(
        modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            title,
            style = PetClinicAppointmentTheme.typography.h2,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(
                horizontal = PetClinicAppointmentTheme.dimensions.grid_2,
                vertical = PetClinicAppointmentTheme.dimensions.grid_1
            )
        )
        Text(
            if(value.isNotEmpty()) value else "-",
            style = PetClinicAppointmentTheme.typography.h2,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(
                horizontal = PetClinicAppointmentTheme.dimensions.grid_2,
                vertical = PetClinicAppointmentTheme.dimensions.grid_1
            )
        )
        Divider(Modifier.fillMaxWidth())
    }
}