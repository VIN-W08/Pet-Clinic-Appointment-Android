package com.example.vin.petclinicappointment.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.lang.IllegalArgumentException

enum class WindowType { Compact, Medium, Expanded }

@Composable
fun rememberWindowInfo(): WindowInfo {
    val configuration = LocalConfiguration.current
    return WindowInfo(
        width = Width(
            configuration.screenWidthDp.dp,
            getWindowWidthType(configuration.screenWidthDp.dp),
        ),
        height = Height(
            configuration.screenHeightDp.dp,
            getWindowHeightType(configuration.screenHeightDp.dp),
        ),

    )
}

data class WindowInfo(
    val width: Width,
    val height: Height
)

data class Width (
    val size: Dp,
    val type: WindowType
    )

data class Height (
    val size: Dp,
    val type: WindowType
)


private fun getWindowWidthType(widthSize: Dp): WindowType =
    when {
        widthSize < 0.dp -> throw IllegalArgumentException("Window Width Type Not Found")
        widthSize < 600.dp -> WindowType.Compact
        widthSize < 840.dp -> WindowType.Medium
        else -> WindowType.Expanded
    }

private fun getWindowHeightType(heightSize: Dp): WindowType =
    when {
        heightSize < 0.dp -> throw IllegalArgumentException("Window Height Type Not Found")
        heightSize < 480.dp -> WindowType.Compact
        heightSize < 900.dp -> WindowType.Medium
        else -> WindowType.Expanded
    }