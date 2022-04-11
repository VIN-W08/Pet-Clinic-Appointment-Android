package com.example.vin.petclinicappointment.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

enum class Metric {Width, Height}

@Composable
fun SetSizeByWindowType(metric: Metric, compactSize: Dp, mediumSize: Dp, expandedSize: Dp): Dp {
    val windowInfo = rememberWindowInfo()
    when(metric) {
        Metric.Width -> {
            if (windowInfo.width.type === WindowType.Compact) return compactSize
            if (windowInfo.width.type === WindowType.Medium) return mediumSize
            if (windowInfo.width.type === WindowType.Expanded) return expandedSize
        }
        Metric.Height -> {
            if (windowInfo.height.type === WindowType.Compact) return compactSize
            if (windowInfo.height.type === WindowType.Medium) return mediumSize
            if (windowInfo.height.type === WindowType.Expanded) return expandedSize
        }
    }
    throw IllegalArgumentException("Metric Not Found")
}