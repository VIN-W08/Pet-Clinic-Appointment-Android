package com.example.vin.petclinicappointment.ui.components.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.vin.petclinicappointment.ui.theme.SilverSand

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetScaffold(
    scaffoldState: BottomSheetScaffoldState,
    sheetContent: @Composable (ColumnScope.() -> Unit),
    content: @Composable () -> Unit
){
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            BottomSheetHolder()
            sheetContent()
        },
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStartPercent = 10, topEndPercent = 10)
    ) { content() }
}

@Composable
fun BottomSheetHolder(){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .height(45.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        Canvas(
            modifier = Modifier
                .width(80.dp)
                .height(5.dp)
                .background(SilverSand)
        ) {
            drawRect(
                SilverSand,
                style = Stroke(
                    width = 2.dp.toPx(),
                    pathEffect = PathEffect.cornerPathEffect(4.dp.toPx())
                )
            )
        }
    }
}