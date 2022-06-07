package com.example.vin.petclinicappointment.ui.components.petclinic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NavigateBefore
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.vin.petclinicappointment.ui.components.common.IconButton
import com.example.vin.petclinicappointment.ui.theme.Black_50
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun LocationMapPage(
    lat: Double,
    lon: Double,
    markerTitle: String,
    navigateBack: () -> Unit
) {
    val cameraPositionState = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(LatLng(lat, lon), 15f)
    }

    Surface(
        Modifier.fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
        ) {
            Marker(
                position = LatLng(lat, lon),
                title = markerTitle
            )
        }
        Box(
            Modifier.fillMaxSize()
        ) {
            IconButton(
                icon = Icons.Rounded.NavigateBefore,
                contentDescription = "arrow_back",
                hasBorder = true,
                onClick = { navigateBack() },
                containerModifier = Modifier
                    .padding(PetClinicAppointmentTheme.dimensions.grid_2)
                    .background(Black_50, CircleShape),
                tint = Color.White
            )
        }
    }
}