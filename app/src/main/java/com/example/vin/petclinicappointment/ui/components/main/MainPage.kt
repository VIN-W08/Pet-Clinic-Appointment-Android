package com.example.vin.petclinicappointment.ui.components.main

import androidx.annotation.StringRes
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.vin.petclinicappointment.MainAppState
import com.example.vin.petclinicappointment.R
import com.example.vin.petclinicappointment.ui.components.history.HistoryPage
import com.example.vin.petclinicappointment.ui.components.appointment.AppointmentPage
import com.example.vin.petclinicappointment.ui.components.home.CustomerHomePage

fun NavGraphBuilder.customerMainBottomNavGraph (appState: MainAppState, scaffoldState: ScaffoldState){
    composable(route = "home-customer") {
        CustomerHomePage(
            scaffoldState = scaffoldState,
            navigateToSearchPetClinic = { appState.navigateTo("search-pet-clinic-list")},
            navigateTo = appState::navigateTo,
            getLocation = appState::getLocation,
            getLocationPermissionStatus = appState::getLocationPermissionStatus,
            getGpsEnabledStatus = appState::getGpsEnabledStatus,
        )
    }
    composable(route = "history") {
        HistoryPage(
            navigateToAppointmentDetail = { id -> appState.navigateTo("appointment-detail/$id") },
            navigateBack = appState::navigateBack
        )
    }
    composable(route = "profile"){}
}

fun NavGraphBuilder.clinicMainBottomNavGraph (appState: MainAppState, scaffoldState: ScaffoldState){
    composable(route = "appointment") {
        AppointmentPage(
            navigateToAppointmentDetail = { id -> appState.navigateTo("appointment-detail/$id") },
            navigateToClinicAppointmentHistory = { appState.navigateTo("appointment-history-clinic")}
        )
    }
    composable(route = "service") {}
    composable(route = "profile"){}
}

enum class MainBottomNavTabs(
    @StringRes val title: Int,
    val icon: ImageVector,
    val route: String
) {
    Home(R.string.tab_home, Icons.Rounded.Home, "home-customer"),
    History(R.string.tab_history, Icons.Rounded.History,"history"),
    Appointment(R.string.tab_appointment, Icons.Rounded.BookOnline, "appointment"),
    Service(R.string.tab_service, Icons.Rounded.LocalHospital, "service"),
    Profile(R.string.tab_profile, Icons.Rounded.AccountCircle, "profile");

    companion object {
        fun create(role: String): Array<MainBottomNavTabs> {
            return when (role) {
                "customer" -> arrayOf(Home, History, Profile)
                "clinic" -> arrayOf(Appointment, Service, Profile)
                else -> arrayOf()
            }
        }
    }
}

@Composable
fun MainPage() {
}