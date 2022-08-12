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
import com.example.vin.petclinicappointment.ui.components.appointment.AppointmentDetailPage
import com.example.vin.petclinicappointment.ui.components.history.HistoryPage
import com.example.vin.petclinicappointment.ui.components.appointment.AppointmentPage
import com.example.vin.petclinicappointment.ui.components.appointment.ClinicAppointmentHistoryPage
import com.example.vin.petclinicappointment.ui.components.home.CustomerHomePage
import com.example.vin.petclinicappointment.ui.components.petclinic.*
import com.example.vin.petclinicappointment.ui.components.profile.*
import com.example.vin.petclinicappointment.ui.components.service.*

fun NavGraphBuilder.customerMainNavGraph (appState: MainAppState, scaffoldState: ScaffoldState){
    composable(route = "home/customer") {
        CustomerHomePage(
            scaffoldState = scaffoldState,
            navigateToSearchPetClinic = { appState.navigateTo("search-pet-clinic-list")},
            navigateToAppointmentDetail = { id -> appState.navigateTo("appointment-detail/$id") },
            getLocation = appState::getLocation,
            getLocationPermissionStatus = appState::getLocationPermissionStatus,
            getGpsEnabledStatus = appState::getGpsEnabledStatus,
        )
    }
    composable(route = "search-pet-clinic-list"){
        SearchPetClinicListPage(
            navigateToCurrentLocationMap = { appState.navigateTo("current-location-map") },
            navigateBack = appState::navigateBack,
            selectedLocationState = appState.selectedLocation,
            navigateToDetail = { id -> appState.navigateTo("detail-pet-clinic/$id") },
            scaffoldState = appState.scaffoldState
        )
    }
    composable(route = "current-location-map"){
        CurrentLocationMapPage(
            scaffoldState = scaffoldState,
            navigateBack = appState::navigateBack,
            selectedLocationState = appState.selectedLocation,
            setSelectedLocation = appState::setSelectedLocation,
            deviceLocationState = appState.deviceLocation,
            getDeviceLocation = appState::getDeviceLocation,
            navigateToSearchPetClinic = {
                appState.navigateTo("search-pet-clinic-list",
                    "search-pet-clinic-list")
            }
        )
    }
    composable(route = "detail-pet-clinic/{id}"){ navBackStackEntry ->
        PetClinicDetailPage(
            id = navBackStackEntry.arguments?.getString("id")?.toInt() ?: 0,
            navigateBack = appState::navigateBack,
            navigateToInfo = {id -> appState.navigateTo("info-pet-clinic/$id") },
            navigateToHome = { id -> appState.navigateTo("home/customer", "home/customer")},
            scaffoldState = appState.scaffoldState
        )
    }

    composable(route = "info-pet-clinic/{id}"){ navBackStackEntry ->
        PetClinicInfoPage(
            id = navBackStackEntry.arguments?.getString("id")?.toInt() ?: 0,
            navigateBack = appState::navigateBack,
            navigateToLocationMap = {name, lat, lon -> appState.navigateTo("location-map?name=$name&lat=$lat&lon=$lon")},
            scaffoldState = appState.scaffoldState
        )
    }
    composable(route = "location-map?name={name}&lat={lat}&lon={lon}"){ navBackStackEntry ->
        LocationMapPage(
            lat = navBackStackEntry.arguments?.getString("lat")?.toDouble() ?: 0.0,
            lon = navBackStackEntry.arguments?.getString("lon")?.toDouble() ?: 0.0,
            markerTitle = navBackStackEntry.arguments?.getString("name").toString(),
            navigateBack = appState::navigateBack
        )
    }
    composable(route = "history") {
        HistoryPage(
            navigateToAppointmentDetail = { id -> appState.navigateTo("appointment-detail/$id") },
            navigateBack = appState::navigateBack
        )
    }
    composable(route = "profile/customer"){
        CustomerProfilePage(
            navigateToEditCustomerProfile = { appState.navigateTo("profile/customer/update") },
            navigateToLoginOption = { appState.navigateTo("login-option", "main/customer")},
            scaffoldState = appState.scaffoldState
        )
    }
    composable(route = "profile/customer/update"){
        EditCustomerProfilePage(
            navigateBack = appState::navigateBack,
            navigateToCustomerProfile = { appState.navigateTo("profile/customer", "profile/customer") },
            scaffoldState = appState.scaffoldState
        )
    }
}

fun NavGraphBuilder.clinicMainNavGraph (appState: MainAppState, scaffoldState: ScaffoldState){
    composable(route = "appointment") {
        AppointmentPage(
            navigateToAppointmentDetail = { id -> appState.navigateTo("appointment-detail/$id") },
            navigateToClinicAppointmentHistory = { appState.navigateTo("appointment-history-clinic")},
            scaffoldState = appState.scaffoldState
        )
    }
    composable(route = "appointment-detail/{id}"){ navBackStackEntry ->
        AppointmentDetailPage(
            id = navBackStackEntry.arguments?.getString("id")?.toInt() ?: 0,
            navigateBack = appState::navigateBack,
            scaffoldState = appState.scaffoldState
        )
    }

    composable(route = "appointment-history-clinic"){
        ClinicAppointmentHistoryPage(
            navigateToAppointmentDetail = { id-> appState.navigateTo("appointment-detail/$id")} ,
            navigateBack = appState::navigateBack,
            scaffoldState = appState.scaffoldState
        )
    }

    composable(route = "service") {
        ServicePage(
            navigateToServiceDetail = { id -> appState.navigateTo("service/detail/$id")},
            navigateToAddService = { appState.navigateTo("service/add") },
            scaffoldState = appState.scaffoldState
        )
    }

    composable(route = "service/detail/{id}") {
        ServiceDetailPage(
            serviceId = it.arguments?.getString("id")?.toInt() ?: 0,
            navigateToUpdateService = { id -> appState.navigateTo("service/update/$id")  },
            navigateToAddServiceSchedule = { serviceId -> appState.navigateTo("service/schedule/add/$serviceId") },
            navigateToServiceScheduleUpdate = { serviceId, id -> appState.navigateTo("service/schedule/update/$serviceId/$id") },
            navigateBack = appState::navigateBack,
            scaffoldState = appState.scaffoldState
        )
    }

    composable(route = "service/update/{id}") {
        ServiceInputPage(
            serviceId = it.arguments?.getString("id")?.toInt() ?: 0,
            pageType = "update",
            navigateBack = appState::navigateBack,
            navigateToService = { appState.navigateTo("service", "service") },
            navigateToServiceDetail = { id -> appState.navigateTo("service/detail/$id", "service/detail/{id}") },
            scaffoldState = appState.scaffoldState
        )
    }
    composable(route = "service/add") {
        ServiceInputPage(
            pageType = "add",
            navigateBack = appState::navigateBack,
            navigateToService = { appState.navigateTo("service", "service") },
            scaffoldState = appState.scaffoldState
        )
    }
    composable(route = "service/schedule/add/{serviceId}") {
        ServiceScheduleInputPage(
            pageType = "add",
            serviceId = it.arguments?.getString("serviceId")?.toInt() ?: 0,
            navigateBack = appState::navigateBack,
            navigateToServiceDetail = { id -> appState.navigateTo("service/detail/$id", "service/detail/{id}")},
            scaffoldState = appState.scaffoldState
        )
    }
    composable(route = "service/schedule/update/{serviceId}/{id}") {
        ServiceScheduleInputPage(
            serviceId = it.arguments?.getString("serviceId")?.toInt() ?: 0,
            pageType = "update",
            serviceScheduleId = it.arguments?.getString("id")?.toInt() ?: 0,
            navigateBack = appState::navigateBack,
            navigateToServiceDetail = { id -> appState.navigateTo("service/detail/$id", "service/detail/{id}")},
            scaffoldState = appState.scaffoldState
        )
    }
    composable(route = "profile/clinic"){
        ClinicProfilePage(
            navigateToEditClinicProfile = { appState.navigateTo("profile/clinic/update") },
            navigateToLoginOption = { appState.navigateTo("login-option", "main/clinic") },
            navigateToClinicSchedule = { appState.navigateTo("clinic/schedule") },
            scaffoldState = appState.scaffoldState
        )
    }
    composable(route = "profile/clinic/update"){
        EditClinicProfilePage(
            navigateToProfile = { appState.navigateTo("profile/clinic", "profile/clinic/update") },
            navigateBack = appState::navigateBack,
            scaffoldState = appState.scaffoldState
        )
    }
    composable(route = "clinic/schedule"){
        ClinicSchedulePage(
            navigateBack = appState::navigateBack,
            navigateToClinicScheduleAdd = { appState.navigateTo("clinic/schedule/add")},
            navigateToClinicScheduleUpdate = { id -> appState.navigateTo("clinic/schedule/update/$id")},
            scaffoldState = appState.scaffoldState
        )
    }

    composable(route = "clinic/schedule/update/{id}"){
        ClinicScheduleInputPage(
            id = it.arguments?.getString("id")?.toInt() ?: 0,
            pageType = "update",
            navigateBack = appState::navigateBack,
            navigateToClinicSchedule = { appState.navigateTo("clinic/schedule", "clinic/schedule") },
            scaffoldState = scaffoldState
        )
    }

    composable(route = "clinic/schedule/add"){
        ClinicScheduleInputPage(
            pageType = "add",
            navigateBack = appState::navigateBack,
            navigateToClinicSchedule = { appState.navigateTo("clinic/schedule", "clinic/schedule") },
            scaffoldState = scaffoldState
        )
    }
}

enum class MainBottomNavTabs(
    @StringRes val title: Int,
    val icon: ImageVector,
    val route: String
) {
    Home(R.string.tab_home, Icons.Rounded.Home, "home/customer"),
    History(R.string.tab_history, Icons.Rounded.History,"history"),
    Appointment(R.string.tab_appointment, Icons.Rounded.BookOnline, "appointment"),
    Service(R.string.tab_service, Icons.Rounded.MedicalServices, "service"),
    Profile(R.string.tab_profile, Icons.Rounded.AccountCircle, "profile/customer"),
    ClinicProfile(R.string.tab_profile, Icons.Rounded.AccountCircle, "profile/clinic");

    companion object {
        fun create(role: String): Array<MainBottomNavTabs> {
            return when (role) {
                "customer" -> arrayOf(Home, History, Profile)
                "clinic" -> arrayOf(Appointment, Service, ClinicProfile)
                else -> arrayOf()
            }
        }
    }
}

@Composable
fun MainPage() {
}