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
fun ServiceStatusLabel(
    statusCode: Boolean,
    hasBackground: Boolean = true,
    containerModifier: Modifier = Modifier,
    modifier: Modifier = Modifier
){
    val serviceStatusBackgroundColorMap = mapOf(
        false to VenetianRed,
        true to MagicMint
    )

    val serviceStatusTextColorMap = mapOf(
        false to DeepCarminePink,
        true to VividMalachite
    )

    val serviceStatusMap = mapOf(
        false to "non aktif",
        true to "aktif"
    )

    var statusText = serviceStatusMap[statusCode]
    if(statusText !== null) {
        statusText = statusText.replaceFirstChar { it.uppercase() }
    }
    val statusBackgroundColor = serviceStatusBackgroundColorMap[statusCode]
    val statusTextColor = serviceStatusTextColorMap[statusCode]

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