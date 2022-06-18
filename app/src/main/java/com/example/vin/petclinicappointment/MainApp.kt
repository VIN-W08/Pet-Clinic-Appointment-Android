package com.example.vin.petclinicappointment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.vin.petclinicappointment.ui.components.appointment.AppointmentDetailPage
import com.example.vin.petclinicappointment.ui.components.appointment.ClinicAppointmentHistoryPage
import com.example.vin.petclinicappointment.ui.components.common.Snackbar
import com.example.vin.petclinicappointment.ui.components.login.LoginOptionsPage
import com.example.vin.petclinicappointment.ui.components.login.LoginPage
import com.example.vin.petclinicappointment.ui.components.main.*
import com.example.vin.petclinicappointment.ui.components.petclinic.*
import com.example.vin.petclinicappointment.ui.components.sign_up.SignUpPage
import java.util.*

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
    val locale = Locale("in", "ID")
    val context = LocalContext.current
    Locale.setDefault(locale)
    context.resources.configuration.setLocale(locale)

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
    ){ customerMainNavGraph(appState, scaffoldState) }

    navigation(
        route = "main/clinic",
        startDestination = "appointment"
    ){ clinicMainNavGraph(appState, scaffoldState) }

    composable(route = "login-option"){
        LoginOptionsPage (
            navigateToLogin = {appState.navigateTo("login", "sign-up")},
            userRole = appState.userRole
        )
    }

    composable(route = "login") {
        LoginPage(
            navigateToCustomerHome = {
                appState.setMainBottomNav()
                appState.navigateTo("main/customer", "login")
                                     },
            navigateToSignUp = { appState.navigateTo("sign-up", "login") },
            userRole = appState.userRole,
            scaffoldState = appState.scaffoldState,
            navigateToAppointment = {
                appState.setMainBottomNav()
                appState.navigateTo("main/clinic", "login")
            }
        )
    }

    composable(route = "sign-up") {
        SignUpPage(
            navigateToLogin = { appState.navigateTo("login", "sign-up") },
            navigateToHome = {
                appState.setMainBottomNav()
                when(appState.getUserRole()) {
                    "customer" -> appState.navigateTo("main/customer", "login")
                    "clinic" -> appState.navigateTo("main/clinic", "login")
                    else -> throw IllegalArgumentException()
                }
            },
            scaffoldState = appState.scaffoldState,
            userRole = appState.userRole.value ?: ""
        )
    }
}