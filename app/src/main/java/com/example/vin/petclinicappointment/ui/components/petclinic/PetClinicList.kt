package com.example.vin.petclinicappointment.ui.components.petclinic

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
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
import com.example.vin.petclinicappointment.R
import com.example.vin.petclinicappointment.data.model.PetClinic
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import com.example.vin.petclinicappointment.ui.theme.RipeMango

@Composable
fun PetClinicList(
    petClinicList: List<PetClinic>,
    navigateToDetail: (id: Int) -> Unit
){
    Column(
        Modifier.fillMaxHeight()
    ) {
        LazyColumn {
            items(petClinicList) { petClinic ->
                PetClinicListItem(
                    modifier =  Modifier.height(PetClinicAppointmentTheme.dimensions.grid_4 * 3),
                    petClinic,
                    navigateToDetail = navigateToDetail
                )
            }
        }
    }
}

@Composable
fun PetClinicListItem(
    modifier: Modifier = Modifier,
    petClinic: PetClinic,
    navigateToDetail: (id: Int) -> Unit
){
    val clinicAddress = petClinic.address
    Row (
        modifier
            .fillMaxWidth()
            .clickable { petClinic.id?.let { navigateToDetail(it) } }
            .padding(
                start = PetClinicAppointmentTheme.dimensions.grid_2,
                end = PetClinicAppointmentTheme.dimensions.grid_2
            ),
        horizontalArrangement = Arrangement.spacedBy(PetClinicAppointmentTheme.dimensions.grid_2),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(
                id = R.drawable.register_success),
            modifier = Modifier
                .fillMaxWidth(0.25f)
                .fillMaxHeight(0.8f)
                .clip(RoundedCornerShape(PetClinicAppointmentTheme.dimensions.grid_1)),
            contentScale = ContentScale.Fit,
            contentDescription = null
        )
        Column(
            Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            petClinic.name?.let {
                Text(
                    it,
                    style = PetClinicAppointmentTheme.typography.h2,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = PetClinicAppointmentTheme.dimensions.grid_1)
                )
            }
            Column {
                if (clinicAddress != null) {
                    Text(
                        clinicAddress,
                        style = PetClinicAppointmentTheme.typography.h3,
                        fontWeight = FontWeight.Light
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "rating star",
                        tint = RipeMango,
                        modifier = Modifier.size(PetClinicAppointmentTheme.dimensions.grid_2)
                    )
                    Text(
                        "${petClinic.rating})",
                        style = PetClinicAppointmentTheme.typography.h3,
                        fontWeight = FontWeight.Light
                    )
                }
            }
        }
    }
}