package com.example.vin.petclinicappointment.ui.components.petclinic

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.vin.petclinicappointment.R
import com.example.vin.petclinicappointment.data.model.PetClinic
import com.example.vin.petclinicappointment.ui.theme.RipeMango

@Composable
fun PetClinicList(petClinicList: List<PetClinic>){
    Box {
        Column {
            LazyColumn {
                items(petClinicList) { petClinic ->
                    PetClinicListItem(petClinic)
                }
            }
        }
    }
}

@Composable
fun PetClinicListItem(petClinic: PetClinic){
    Row (
        Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(id = R.drawable.register_success),
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Fit,
            contentDescription = null
        )
        Column {
            Text(
                petClinic.name,
                style = MaterialTheme.typography.h2,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                petClinic.address,
                style = MaterialTheme.typography.h3,
                fontWeight = FontWeight.Light
            )
            Row (
                verticalAlignment = Alignment.CenterVertically
                    ){
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "rating_star",
                    tint = RipeMango
                )
                Text(
                    "${petClinic.rating.toString()} (${petClinic.ratingCount.toString()})",
                    style = MaterialTheme.typography.h3,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}