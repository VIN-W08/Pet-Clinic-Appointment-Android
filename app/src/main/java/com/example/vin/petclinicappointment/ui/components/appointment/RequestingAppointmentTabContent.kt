package com.example.vin.petclinicappointment.ui.components.appointment

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.vin.petclinicappointment.R
import com.example.vin.petclinicappointment.data.model.AppointmentDetail
import com.example.vin.petclinicappointment.ui.components.common.MessageView
import com.example.vin.petclinicappointment.ui.components.common.ProgressIndicatorView
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme

@Composable
fun RequestingAppointmentTabContent(
    navigateToAppointmentDetail: (id: Int) -> Unit,
    currentPage: Int,
    appointmentViewModel: AppointmentViewModel = hiltViewModel(),
){
    val requestingAppointmentList = if(currentPage == 1) appointmentViewModel.requestingAppointmentList.collectAsLazyPagingItems() else null

    Surface(
        color = PetClinicAppointmentTheme.colors.background
    ){
        Column(
            Modifier.fillMaxSize()
        ) {
            requestingAppointmentList.apply {
                if(this !== null) {
                    AppointmentList(
                        appointmentList = this,
                        navigateToAppointmentDetail = navigateToAppointmentDetail
                    )
                    when {
                        loadState.refresh == LoadState.Loading -> {
                            ProgressIndicatorView(Modifier.fillMaxSize())
                        }
                        loadState.append == LoadState.Loading -> {
                            ProgressIndicatorView(Modifier.fillMaxWidth())
                        }
                        (loadState.refresh is LoadState.NotLoading &&
                                this.itemCount == 0) -> {
                            MessageView(
                                message = stringResource(R.string.no_requesting_appointment),
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}