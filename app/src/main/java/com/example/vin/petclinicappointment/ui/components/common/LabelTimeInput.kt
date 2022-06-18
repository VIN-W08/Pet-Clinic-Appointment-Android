package com.example.vin.petclinicappointment.ui.components.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import java.time.LocalTime

@Composable
fun LabelTimeInput(
    label: String,
    value: String,
    onTimeValueChange: (time: LocalTime) -> Unit,
    required: Boolean = true,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            Modifier.fillMaxWidth(0.4f)
        ) {
            Text(
                label,
                style = PetClinicAppointmentTheme.typography.h3
            )
            if (required) {
                Text(
                    "*",
                    style = PetClinicAppointmentTheme.typography.h3,
                    color = Color.Red
                )
            }
        }
        Row(
            Modifier.fillMaxWidth(0.5f)
        ) {
            TimePicker(
                value = value,
                onTimeValueChange = onTimeValueChange,
            )
        }
    }
}