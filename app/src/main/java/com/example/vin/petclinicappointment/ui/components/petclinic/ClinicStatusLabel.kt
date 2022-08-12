package com.example.vin.petclinicappointment.ui.components.petclinic

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.vin.petclinicappointment.ui.theme.*

@Composable
fun ClinicStatusLabel(
    statusCode: Boolean,
    style: TextStyle = PetClinicAppointmentTheme.typography.h3,
    modifier: Modifier = Modifier
){

    val clinicStatusTextColorMap = mapOf(
        false to CarminePink,
        true to VeryLightMalachite
    )

    val clinicStatusToStringMap = mapOf(
        false to "tutup",
        true to "buka"
    )

    var statusText = clinicStatusToStringMap[statusCode]
    if(statusText !== null) {
        statusText = statusText.replaceFirstChar { it.uppercase() }
    }
    val statusTextColor = clinicStatusTextColorMap[statusCode]

    if(statusText !== null) {
        if (statusTextColor != null) {
            Text(
                statusText,
                style = style,
                fontWeight = FontWeight.SemiBold,
                color = statusTextColor,
                modifier = modifier
            )
        }
    }
}