package com.example.vin.petclinicappointment.ui.components.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun View(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable () -> Unit
){
    val interactionSource = MutableInteractionSource()
    Box (
        modifier = modifier
            .clickable(
                enabled = onClick !== null ,
                onClick = { if (onClick !== null) onClick() },
                indication = null,
                interactionSource = interactionSource
            ),
        contentAlignment = contentAlignment
    ){
        content()
    }
}