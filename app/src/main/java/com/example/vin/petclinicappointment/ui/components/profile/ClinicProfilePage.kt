package com.example.vin.petclinicappointment.ui.components.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vin.petclinicappointment.ui.components.common.Image
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import com.example.vin.petclinicappointment.ui.components.common.AppButton
import com.example.vin.petclinicappointment.ui.components.common.CircularProgressIndicator
import com.example.vin.petclinicappointment.ui.components.common.View
import kotlinx.coroutines.runBlocking

@Composable
fun ClinicProfilePage(
    clinicProfileViewModel: ClinicProfileViewModel = hiltViewModel(),
    navigateToEditClinicProfile: () -> Unit,
    navigateToLoginOption: () -> Unit
){
    var progressIndicatorVisible by rememberSaveable { mutableStateOf(false) }
    val clinicName by clinicProfileViewModel.clinicName.collectAsState()
    val clinicEmail by clinicProfileViewModel.clinicEmail.collectAsState()
    val clinicAddress by clinicProfileViewModel.clinicAddress.collectAsState()
    val clinicLatitude by clinicProfileViewModel.clinicLatitude.collectAsState()
    val clinicLongitude by clinicProfileViewModel.clinicLongitude.collectAsState()
    val clinicPhoneNum by clinicProfileViewModel.clinicPhoneNum.collectAsState()
    val clinicImage by clinicProfileViewModel.clinicImage.collectAsState()
    val clinicLocationName by clinicProfileViewModel.clinicLocationName.collectAsState()

    fun logout(){
        runBlocking {
            progressIndicatorVisible = true
            clinicProfileViewModel.logoutClinic()
            progressIndicatorVisible = false
            navigateToLoginOption()
        }
    }

    LaunchedEffect(Unit){
        progressIndicatorVisible = true
        clinicProfileViewModel.getClinicData()
        clinicProfileViewModel.getVillageDetail()
        clinicProfileViewModel.getDistrictDetail()
        clinicProfileViewModel.getCityDetail()
        clinicProfileViewModel.getProvinceDetail()
        progressIndicatorVisible = false
    }

    Surface(
        color = PetClinicAppointmentTheme.colors.background
    ){
        Column {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(
                        start = PetClinicAppointmentTheme.dimensions.grid_2,
                        end = PetClinicAppointmentTheme.dimensions.grid_2,
                        top = PetClinicAppointmentTheme.dimensions.grid_2,
                        bottom = PetClinicAppointmentTheme.dimensions.grid_4
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "Profil Klinik",
                    style = PetClinicAppointmentTheme.typography.h1
                )
            }
            if(!progressIndicatorVisible) {
                Column(
                    Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    Column {
                        View(
                            Modifier
                                .padding(
                                    start = PetClinicAppointmentTheme.dimensions.grid_2,
                                    end = PetClinicAppointmentTheme.dimensions.grid_2,
                                    bottom = PetClinicAppointmentTheme.dimensions.grid_2
                                )
                                .fillMaxWidth()
                                .height(PetClinicAppointmentTheme.dimensions.grid_5 * 5),
                            contentAlignment = Alignment.Center
                        ) {
                            if (clinicImage.isNotEmpty()) {
                                Image(
                                    base64 = clinicImage,
                                    contentDescription = "clinic image",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Canvas(modifier = Modifier.fillMaxSize()){
                                    drawRoundRect(
                                        color = Color.Black,
                                        style = Stroke(width = 4f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 10f))
                                    )
                                }
                                Box(
                                    Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Foto klinik tidak tersedia")
                                }
                            }
                        }
                        PrefTextView(title = "Nama Klinik", value = clinicName)
                        PrefTextView(title = "Email", value = clinicEmail)
                        PrefTextView(title = "No. Telp", value = clinicPhoneNum)
                        PrefTextView(title = "Alamat Klinik", value = clinicAddress)
                        PrefTextView(title = "Kecamatan, Kelurahan, Kota/Kabupaten, Provinsi",
                            value = clinicLocationName)
                        PrefTextView(title = "Lintang",
                            value = if (clinicLatitude !== null) clinicLatitude.toString() else "")
                        PrefTextView(title = "Bujur",
                            value = if (clinicLongitude !== null) clinicLongitude.toString() else "")
                    }
                    AppButton(
                        onClick = navigateToEditClinicProfile,
                        modifier = Modifier
                            .padding(
                                start = PetClinicAppointmentTheme.dimensions.grid_2,
                                top = PetClinicAppointmentTheme.dimensions.grid_4,
                                end = PetClinicAppointmentTheme.dimensions.grid_2,
                                bottom = PetClinicAppointmentTheme.dimensions.grid_2
                            )
                            .fillMaxWidth()
                            .height(PetClinicAppointmentTheme.dimensions.grid_5_5),
                        colors = ButtonDefaults.buttonColors(PetClinicAppointmentTheme.colors.secondary),
                        shape = RoundedCornerShape(PetClinicAppointmentTheme.dimensions.grid_5)
                    ) {
                        Text("Ubah Profil")
                    }
                    AppButton(
                        onClick = { logout() },
                        modifier = Modifier
                            .padding(
                                start = PetClinicAppointmentTheme.dimensions.grid_2,
                                top = PetClinicAppointmentTheme.dimensions.grid_4,
                                end = PetClinicAppointmentTheme.dimensions.grid_2,
                                bottom = PetClinicAppointmentTheme.dimensions.grid_2
                            )
                            .fillMaxWidth()
                            .height(PetClinicAppointmentTheme.dimensions.grid_5_5),
                        colors = ButtonDefaults.buttonColors(PetClinicAppointmentTheme.colors.error),
                        shape = RoundedCornerShape(PetClinicAppointmentTheme.dimensions.grid_5)
                    ) {
                        Text(
                            "Keluar",
                            color = PetClinicAppointmentTheme.colors.onError
                        )
                    }
                }
            }
        }
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(visible = progressIndicatorVisible)
        }
    }
}

@Composable
fun PrefTextView(
    title: String,
    value: String,
    modifier: Modifier = Modifier
){
    Column(
        modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            title,
            style = PetClinicAppointmentTheme.typography.h2,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(PetClinicAppointmentTheme.dimensions.grid_1)
        )
        Text(
            if(value.isNotEmpty()) value else "-",
            style = PetClinicAppointmentTheme.typography.h2,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(PetClinicAppointmentTheme.dimensions.grid_1)
        )
        Divider(Modifier.fillMaxWidth())
    }
}