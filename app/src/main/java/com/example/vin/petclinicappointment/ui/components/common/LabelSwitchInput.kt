package com.example.vin.petclinicappointment.ui.components.common

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme

@Composable
fun LabelSwitchInput(
    label: String,
    value: Boolean,
    onSwitchChange: (active: Boolean) -> Unit,
    modifier: Modifier = Modifier
){
    Row (
        modifier = modifier
            .padding(
                bottom = PetClinicAppointmentTheme.dimensions.grid_4
            )
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = PetClinicAppointmentTheme.dimensions.grid_2)
                .weight(0.4f)
        ) {
            Text(
                label,
                style = PetClinicAppointmentTheme.typography.h3
            )
        }
        Row(
            Modifier
                .padding(end = PetClinicAppointmentTheme.dimensions.grid_2)
                .weight(0.6f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = value,
                onCheckedChange = onSwitchChange
            )
        }
    }
}