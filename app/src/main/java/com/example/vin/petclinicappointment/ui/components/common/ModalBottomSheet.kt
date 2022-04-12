package com.example.vin.petclinicappointment.ui.components.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.vin.petclinicappointment.ui.theme.SilverSand

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalBottomSheet(
    modifier: Modifier = Modifier,
    modalBottomSheetState: ModalBottomSheetState,
    sheetContent: @Composable ColumnScope.() -> Unit,
    scrimColor: Color = Color.Unspecified,
    content: @Composable () -> Unit
){
    ModalBottomSheetLayout(
        modifier = modifier,
        sheetState = modalBottomSheetState,
        sheetContent = {
            ModalBottomSheetHolder()
            sheetContent()
        },
        scrimColor = scrimColor,
        sheetShape = RoundedCornerShape(topStartPercent = 5, topEndPercent = 5)
    ){ content() }
}

@Composable
fun ModalBottomSheetHolder(){
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
                .width(60.dp)
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