package com.example.vin.petclinicappointment.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.material.IconButton
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme

@Composable
fun IconButton(
    buttonModifier: Modifier = Modifier,
    modifier: Modifier = Modifier,
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
){
    IconButton (
        modifier = buttonModifier
            .size(PetClinicAppointmentTheme.dimensions.grid_4)
            .background(Color.Transparent),
        onClick = onClick
    ) {
        Icon(
            modifier = modifier
                .size(PetClinicAppointmentTheme.dimensions.grid_2),
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}