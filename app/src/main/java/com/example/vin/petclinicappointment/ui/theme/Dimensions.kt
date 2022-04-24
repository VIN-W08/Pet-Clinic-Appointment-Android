package com.example.vin.petclinicappointment.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimensions (
    val grid_0_1: Dp,
    val grid_0_25: Dp,
    val grid_0_5: Dp,
    val grid_1: Dp,
    val grid_1_5: Dp,
    val grid_2: Dp,
    val grid_2_5: Dp,
    val grid_3: Dp,
    val grid_3_5: Dp,
    val grid_4: Dp,
    val grid_4_5: Dp,
    val grid_5: Dp,
    val grid_5_5: Dp,
    val grid_6: Dp,
    val grid_6_5: Dp,
    val grid_7: Dp,
    val grid_7_5: Dp,
    val grid_8: Dp
        )

class DimensionsBuilder {
    fun createDimensionSystem(breakpoint: Int): Dimensions{
        val grid_1 = breakpoint/45.0
        return Dimensions(
            grid_0_1 = (grid_1*0.1).dp,
            grid_0_25 = (grid_1*0.25).dp,
            grid_0_5 = (grid_1*0.5).dp,
            grid_1 = grid_1.dp,
            grid_1_5 = (grid_1*1.5).dp,
            grid_2 = (grid_1*2).dp,
            grid_2_5 = (grid_1*2.5).dp,
            grid_3 = (grid_1*3).dp,
            grid_3_5 = (grid_1*3.5).dp,
            grid_4 = (grid_1*4).dp,
            grid_4_5 = (grid_1*4.5).dp,
            grid_5 = (grid_1*5).dp,
            grid_5_5 = (grid_1*5.5).dp,
            grid_6 = (grid_1*6).dp,
            grid_6_5 = (grid_1* 6.5).dp,
            grid_7 = (grid_1*7).dp,
            grid_7_5 = (grid_1*7.5).dp,
            grid_8 = (grid_1*8).dp,
        )
    }
}

val sw360Dimensions = Dimensions(
    grid_0_1 = 0.8.dp,
    grid_0_25 = 2.dp,
    grid_0_5 = 4.dp,
    grid_1 = 8.dp,
    grid_1_5 = 12.dp,
    grid_2 = 16.dp,
    grid_2_5 = 20.dp,
    grid_3 = 24.dp,
    grid_3_5 = 28.dp,
    grid_4 = 32.dp,
    grid_4_5 = 36.dp,
    grid_5 = 40.dp,
    grid_5_5 = 44.dp,
    grid_6 = 48.dp,
    grid_6_5 = 52.dp,
    grid_7 = 56.dp,
    grid_7_5 = 60.dp,
    grid_8 = 64.dp,
)