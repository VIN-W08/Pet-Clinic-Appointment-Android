package com.example.vin.petclinicappointment.ui.components.petclinic

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.vin.petclinicappointment.R
import com.example.vin.petclinicappointment.data.model.PetClinic
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import com.example.vin.petclinicappointment.ui.components.common.Image
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

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
    val clinicDistance = petClinic.distance

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
        if(petClinic.image !== null) {
            Image(
                base64 = petClinic.image,
                modifier = Modifier
                    .size(PetClinicAppointmentTheme.dimensions.grid_10)
                    .clip(RoundedCornerShape(10)),
                contentScale = ContentScale.Fit,
                contentDescription = null
            )
        }else{
            Image(
                painterResource(id = R.drawable.default_clinic_image),
                modifier = Modifier
                    .size(PetClinicAppointmentTheme.dimensions.grid_10)
                    .clip(RoundedCornerShape(10)),
                contentScale = ContentScale.Fit,
                contentDescription = null
            )
        }
        Column(
            Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            petClinic.name?.let {
                Text(
                    it,
                    style = PetClinicAppointmentTheme.typography.h2,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = PetClinicAppointmentTheme.dimensions.grid_1),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column {
                if (clinicAddress != null) {
                    Text(
                        clinicAddress,
                        style = PetClinicAppointmentTheme.typography.h3,
                        fontWeight = FontWeight.Light,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if(clinicDistance !== null){
                    Text(
                        "${DecimalFormat("#.#").format(clinicDistance).replace(",", ".")} km",
                        style = PetClinicAppointmentTheme.typography.h3,
                        fontWeight = FontWeight.Light
                    )
                }
            }
        }
    }
}