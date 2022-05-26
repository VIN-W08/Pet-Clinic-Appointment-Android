package com.example.vin.petclinicappointment.ui.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vin.petclinicappointment.R
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import com.example.vin.petclinicappointment.ui.components.common.IconButton

@Composable
fun HomeHeader(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigateToPetClinicList: () -> Unit
){
    val username by homeViewModel.username.collectAsState()

    LaunchedEffect(Unit){
        homeViewModel.getUserData()
    }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(PetClinicAppointmentTheme.dimensions.grid_2),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.google_logo),
                        contentDescription = "profile_photo",
                        modifier = Modifier
                            .size(PetClinicAppointmentTheme.dimensions.grid_6)
                            .clip(CircleShape)
                            .padding(end = PetClinicAppointmentTheme.dimensions.grid_2)
                    )
                    Column {
                        Text("Hello",
                            style = PetClinicAppointmentTheme.typography.h2,
                            color = PetClinicAppointmentTheme.colors.onPrimary
                        )
                        Text(username,
                            style = PetClinicAppointmentTheme.typography.h3,
                            color = PetClinicAppointmentTheme.colors.onPrimary
                        )
                    }
                }
                IconButton(
                    icon = Icons.Default.Search,
                    tint = PetClinicAppointmentTheme.colors.onPrimary,
                    contentDescription = "search",
                    hasBorder = true,
                    onClick = { navigateToPetClinicList() },
                    modifier = Modifier
                        .size(PetClinicAppointmentTheme.dimensions.grid_2_5)
//                        .background(PetClinicAppointmentTheme.colors.primary)
                )
            }
}