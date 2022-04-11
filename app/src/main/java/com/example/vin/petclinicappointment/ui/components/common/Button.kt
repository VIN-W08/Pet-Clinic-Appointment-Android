package com.example.vin.petclinicappointment.ui.components.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape

@Composable
fun AppButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: ButtonColors,
    shape: Shape,
    border: BorderStroke? = null,
    disabled: Boolean = false,
    content: @Composable RowScope.() -> Unit
) {
    ProvideTextStyle(value = MaterialTheme.typography.h3) {
        Button(
            onClick = onClick,
            modifier = modifier,
            colors = colors,
            content = content,
            shape = shape,
            border = border,
            enabled = !disabled
        )
    }
}