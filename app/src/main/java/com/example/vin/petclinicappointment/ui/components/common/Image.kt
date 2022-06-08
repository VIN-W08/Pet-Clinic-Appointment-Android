package com.example.vin.petclinicappointment.ui.components.common

import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun Image(
    painter: Painter? = null,
    base64: String? = null,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.None,
    contentDescription: String? = null
){
    if(base64 !== null) {
        val imageByteArray = Base64.decode(base64, Base64.DEFAULT)
        Image(
            bitmap = BitmapFactory.decodeByteArray(
                imageByteArray,
                0,
                imageByteArray.size
            ).asImageBitmap(),
            modifier = modifier,
            contentScale = contentScale,
            contentDescription = contentDescription
        )
    } else if(painter !== null){
        Image(
            painter = painter,
            modifier = modifier,
            contentScale = contentScale,
            contentDescription = contentDescription
        )
    }
}