package com.example.vin.petclinicappointment.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme

@Composable
fun IconButton(
    buttonModifier: Modifier = Modifier,
    containerModifier: Modifier = Modifier,
    modifier: Modifier = Modifier,
    icon: ImageVector,
    tint: Color = Color.Unspecified,
    contentDescription: String,
    hasBorder: Boolean = false,
    onClick: () -> Unit,
){
    val interactionSource = MutableInteractionSource()
    if(hasBorder) {
        Box(
            containerModifier
                .size(PetClinicAppointmentTheme.dimensions.grid_4)
                .background(PetClinicAppointmentTheme.colors.primaryVariant, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = modifier
                    .size(PetClinicAppointmentTheme.dimensions.grid_2)
                    .clickable(
                        indication = null,
                        interactionSource = interactionSource
                    ) { onClick() },
                imageVector = icon,
                contentDescription = contentDescription,
                tint = tint
            )
        }
    }else{
        Icon(
            modifier = modifier
                .size(PetClinicAppointmentTheme.dimensions.grid_2)
                .clickable(
                    indication = null,
                    interactionSource = interactionSource
                ) { onClick() },
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint
        )
    }
//    }
}