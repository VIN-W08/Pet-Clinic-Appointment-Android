package com.example.vin.petclinicappointment

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.vin.petclinicappointment.ui.components.appointment.AppointmentDetailPage
import com.example.vin.petclinicappointment.ui.components.appointment.ClinicAppointmentHistoryPage
import com.example.vin.petclinicappointment.ui.components.common.Snackbar
import com.example.vin.petclinicappointment.ui.components.login.LoginOptionsPage
import com.example.vin.petclinicappointment.ui.components.login.LoginPage
import com.example.vin.petclinicappointment.ui.components.main.MainBottomNavBar
import com.example.vin.petclinicappointment.ui.components.main.clinicMainBottomNavGraph
import com.example.vin.petclinicappointment.ui.components.main.customerMainBottomNavGraph
import com.example.vin.petclinicappointment.ui.components.petclinic.*
import com.example.vin.petclinicappointment.ui.components.sign_up.SignUpPage

@Composable
fun MainApp() {
    val appState = rememberMainAppState()
    val startDestination = if(appState.getUserAuthStatus()) {
        when(appState.getUserRole()) {
           "customer" -> "main/customer"
            "clinic" -> "main/clinic"
            else -> throw IllegalArgumentException()
        }
    } else "login-option"
    val scaffoldState = appState.scaffoldState
    val mainBottomNavBarTabs by appState.mainBottomNavBarTabs.collectAsState()

    LaunchedEffect(Unit){
        if(startDestination == "main/customer" || startDestination == "main/clinic") {
            appState.setMainBottomNav()
        }
    }

    Scaffold (
        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState },
        bottomBar = {
            if(appState.showBottomNavBar) {
                MainBottomNavBar(
                    mainBottomNavBarTabs,
                    appState.currentRoute,
                    appState::navigateMainBottomNavBarTo
                )
            }
        }
    ) {
        Box (
            Modifier
                .fillMaxSize()
                .padding(it)
                ){
            NavHost(
                navController = appState.navController,
                startDestination = startDestination.toString()
            ) {
                appNavGraph(appState, scaffoldState)
            }
            Snackbar(
                modifier = Modifier.align(Alignment.BottomCenter),
                snackbarHostState = scaffoldState.snackbarHostState,
                onClickAction = { scaffoldState.snackbarHostState.currentSnackbarData?.dismiss() }
            )
        }
    }
}

private fun NavGraphBuilder.appNavGraph (appState: MainAppState, scaffoldState: ScaffoldState){
    navigation(
        route = "main/customer",
        startDestination = "home-customer"
    ){
        customerMainBottomNavGraph(appState, scaffoldState)
    }

    navigation(
        route = "main/clinic",
        startDestination = "appointment"
    ){
        clinicMainBottomNavGraph(appState, scaffoldState)
    }

    composable(route = "login-option"){
        LoginOptionsPage (
            navigateToLogin = {appState.navigateTo("login", "sign-up")},
            userRole = appState.userRole
        )
    }
    composable(route = "login") {
        LoginPage(
            navigateToCustomerHome = {
                Log.d("debug1", "role:${appState.userRole.value}")
                appState.setMainBottomNav()
                appState.navigateTo("main/customer", "login")
                                     },
            navigateToSignUp = { appState.navigateTo("sign-up", "login") },
            userRole = appState.userRole,
            scaffoldState = appState.scaffoldState,
            navigateToAppointment = {
                Log.d("debug1", "role:${appState.userRole.value}")
                appState.setMainBottomNav()
                appState.navigateTo("main/clinic", "login")
            }
        )
    }
    composable(route = "sign-up") {
        SignUpPage(
            navigateToLogin = { appState.navigateTo("login", "sign-up")},
            navigateToHome = { appState.navigateTo("main", "login") },
            scaffoldState = appState.scaffoldState,
            userRole = appState.userRole.value ?: ""
        )
    }
    composable(route = "pet-clinic-list/{title}/{type}"){ navBackStackEntry ->
        PetClinicListPage(
            navBackStackEntry.arguments?.getString("title")?:"",
            navBackStackEntry.arguments?.getString("type")?:"",
            navigateBack = appState::navigateBack
        )
    }
    composable(route = "search-pet-clinic-list"){
        SearchPetClinicListPage(
            navigateToCurrentLocationMap = { appState.navigateTo("current-location-map") },
            navigateBack = appState::navigateBack,
            selectedLocationState = appState.selectedLocation,
            navigateToDetail = { id -> appState.navigateTo("detail-pet-clinic/$id") }
        )
    }
    composable(route = "current-location-map"){
        CurrentLocationMapPage(
            scaffoldState = scaffoldState,
            navigateBack = appState::navigateBack,
            selectedLocationState = appState.selectedLocation,
            deviceLocationState = appState.deviceLocation,
            getDeviceLocation = appState::getDeviceLocation
        )
    }
    composable(route = "detail-pet-clinic/{id}"){ navBackStackEntry ->
        PetClinicDetailPage(
            id = navBackStackEntry.arguments?.getString("id")?.toInt() ?: 0,
            navigateBack = appState::navigateBack,
            navigateToInfo = {id -> appState.navigateTo("info-pet-clinic/$id") },
            navigateToHome = { id -> appState.navigateTo("main/customer", "detail-pet-clinic/$id")}
        )
    }

    composable(route = "info-pet-clinic/{id}"){ navBackStackEntry ->
        PetClinicInfoPage(
            id = navBackStackEntry.arguments?.getString("id")?.toInt() ?: 0,
            navigateBack = appState::navigateBack,
            navigateToLocationMap = {name, lat, lon -> appState.navigateTo("location-map?name=$name&lat=$lat&lon=$lon")}
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

    composable(route = "appointment-detail/{id}"){ navBackStackEntry ->
        AppointmentDetailPage(
            id = navBackStackEntry.arguments?.getString("id")?.toInt() ?: 0,
            navigateBack = appState::navigateBack
        )
    }

    composable(route = "appointment-history-clinic"){
        ClinicAppointmentHistoryPage(
           navigateToAppointmentDetail = { id-> appState.navigateTo("appointment-detail/$id")} ,
            navigateBack = appState::navigateBack
        )
    }
}