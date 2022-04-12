package com.example.vin.petclinicappointment.ui.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vin.petclinicappointment.R

@Composable
fun HomeHeader(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigateToPetClinicList: () -> Unit
){
    val username by homeViewModel.username.collectAsState()

    LaunchedEffect(Unit){
        homeViewModel.getUserData()
    }

    Row (
        Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
            ){
        Row {
            Image(
                painter = painterResource(id = R.drawable.google_logo),
                contentDescription = "profile_photo",
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape)
                    .padding(end = 15.dp)
            )
            Column {
                Text("Hello",
                    style = MaterialTheme.typography.h2)
                Text(username,
                    style = MaterialTheme.typography.h3)
            }
        }
        IconButton(onClick = { navigateToPetClinicList() }) {
            Icon(imageVector = Icons.Default.Search, contentDescription = "search")
        }
    }
}