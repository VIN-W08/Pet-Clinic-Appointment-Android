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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vin.petclinicappointment.R
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import com.example.vin.petclinicappointment.ui.components.common.IconButton
import com.example.vin.petclinicappointment.ui.theme.Black_50

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
                if(username.isNotEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Hai, ",
                            style = PetClinicAppointmentTheme.typography.h2,
                            color = PetClinicAppointmentTheme.colors.onPrimary,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            username,
                            style = PetClinicAppointmentTheme.typography.h2,
                            color = PetClinicAppointmentTheme.colors.onPrimary,
                        )
                    }
                }
                IconButton(
                    icon = Icons.Default.Search,
                    tint = PetClinicAppointmentTheme.colors.onPrimary,
                    contentDescription = "search",
                    hasBorder = true,
                    onClick = { navigateToPetClinicList() },
                    containerModifier = Modifier.background(PetClinicAppointmentTheme.colors.primaryVariant, CircleShape),
                    modifier = Modifier
                        .size(PetClinicAppointmentTheme.dimensions.grid_2_5)
                )
            }
}