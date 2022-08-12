package com.example.vin.petclinicappointment.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.example.vin.petclinicappointment.ui.theme.*

@Composable
fun StatusLabel(
    statusCode: Int,
    hasBackground: Boolean,
    modifier: Modifier = Modifier,
    containerModifier: Modifier = Modifier
){
    val apppointmentStatusBackgroundColorMap = mapOf(
        0 to VeryPaleYellow,
        1 to MagicMint,
        2 to VenetianRed,
        3 to VenetianRed,
        4 to MagicMint
    )

    val appointmentStatusCodeToColorMap = mapOf(
        0 to Sandstorm,
        1 to VeryLightMalachite,
        2 to CarminePink,
        3 to CarminePink,
        4 to VeryLightMalachite
    )

    val appointmentStatusCodeToMap = mapOf(
        0 to "mengajukan",
        1 to "disetujui",
        2 to "tidak disetujui",
        3 to "dibatalkan",
        4 to "selesai"
    )

    var statusText = appointmentStatusCodeToMap[statusCode]
    if(statusText !== null) {
        statusText = statusText.replaceFirstChar { it.uppercase() }
    }
    val statusBackgroundColor = apppointmentStatusBackgroundColorMap[statusCode]
    val statusTextColor = appointmentStatusCodeToColorMap[statusCode]

    if(statusText !== null) {
        if (hasBackground &&
            statusBackgroundColor?.equals(null) == false &&
            statusTextColor?.equals(null) == false
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = containerModifier
                    .clip(RoundedCornerShape(PetClinicAppointmentTheme.dimensions.grid_1))
                    .background(statusBackgroundColor)
            ) {
                Text(
                    statusText,
                    fontWeight = FontWeight.SemiBold,
                    color = statusTextColor,
                    modifier = modifier.padding(
                        horizontal = PetClinicAppointmentTheme.dimensions.grid_1_5,
                        vertical = PetClinicAppointmentTheme.dimensions.grid_1
                    )
                )
            }
        } else {
            if(statusTextColor?.equals(null) == false) {
                Text(
                    statusText,
                    fontWeight = FontWeight.SemiBold,
                    color = statusTextColor,
                    modifier = modifier
                )
            }
        }
    }
}