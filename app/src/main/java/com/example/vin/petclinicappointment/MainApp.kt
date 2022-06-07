package com.example.vin.petclinicappointment

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
import com.example.vin.petclinicappointment.ui.components.common.Snackbar
import com.example.vin.petclinicappointment.ui.components.login.LoginOptionsPage
import com.example.vin.petclinicappointment.ui.components.login.LoginPage
import com.example.vin.petclinicappointment.ui.components.main.MainBottomNavBar
import com.example.vin.petclinicappointment.ui.components.main.mainBottomNavGraph
import com.example.vin.petclinicappointment.ui.components.petclinic.*
import com.example.vin.petclinicappointment.ui.components.sign_up.CustomerSignUpPage

@Composable
fun MainApp() {
    val appState = rememberMainAppState()
    val startDestination = if(appState.getUserAuthStatus()) "main" else "login-option"
    val scaffoldState = appState.scaffoldState

    Scaffold (
        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState },
        bottomBar = {
            if(appState.showBottomNavBar) {
                MainBottomNavBar(
                    appState.mainBottomNavBarTabs,
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
                startDestination = startDestination
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
        route = "main",
        startDestination = "home-customer"
    ){
        mainBottomNavGraph(appState, scaffoldState)
    }
    composable(route = "login-option"){
        LoginOptionsPage (
            navigateToLogin = {appState.navigateTo("login", "sign-up")},
            userRole = appState.userRole
        )
    }
    composable(route = "login") {
        LoginPage(
            navigateToCustomerHome = { appState.navigateTo("main", "login") },
            navigateToCustomerCustomerSignUp = { appState.navigateTo("sign-up-customer", "login")},
            userRole = appState.userRole,
            scaffoldState = appState.scaffoldState
        )
    }
    composable(route = "sign-up-customer") {
        CustomerSignUpPage(
            navigateToLogin = { appState.navigateTo("login", "sign-up-customer")},
            navigateToHome = { appState.navigateTo("main", "login") },
            scaffoldState = appState.scaffoldState
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
            navigateToHome = { id -> appState.navigateTo("main", "detail-pet-clinic/$id")}
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
}