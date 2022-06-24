package com.example.vin.petclinicappointment.ui.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme

@Composable
fun LabelSwitchInput(
    label: String,
    value: Boolean,
    onSwitchChange: (active: Boolean) -> Unit,
    modifier: Modifier = Modifier
){
    Row (
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(0.4f)
        ) {
            Text(
                label,
                style = PetClinicAppointmentTheme.typography.h3
            )
        }
        Row(
            Modifier
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