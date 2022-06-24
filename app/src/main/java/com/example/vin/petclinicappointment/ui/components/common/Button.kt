package com.example.vin.petclinicappointment.ui.components.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.example.vin.petclinicappointment.ui.theme.*

enum class ButtonStatus { Success, Error }

@Composable
fun AppButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: ButtonColors? = null,
    buttonStatus: ButtonStatus? = null,
    shape: Shape,
    border: BorderStroke? = null,
    disabled: Boolean = false,
    content: @Composable RowScope.() -> Unit,
) {
    val buttonColor = when(buttonStatus){
        ButtonStatus.Success -> buttonColors(VeryLightMalachite)
        ButtonStatus.Error -> buttonColors(CarminePink)
        else -> if(colors !==null) colors else buttonColors(PetClinicAppointmentTheme.colors.primary)
    }

    ProvideTextStyle(value = MaterialTheme.typography.h3) {
        Button(
            onClick = onClick,
            modifier = modifier,
            colors = buttonColor,
            content = content,
            shape = shape,
            border = border,
            enabled = !disabled
        )
    }
}