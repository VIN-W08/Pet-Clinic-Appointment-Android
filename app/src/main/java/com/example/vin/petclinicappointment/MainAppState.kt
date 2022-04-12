package com.example.vin.petclinicappointment

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vin.petclinicappointment.data.UserDataStore
import com.example.vin.petclinicappointment.data.repository.UserRepository
import com.example.vin.petclinicappointment.ui.components.main.MainBottomNavTabs
import kotlinx.coroutines.*

@Composable
fun rememberMainAppState(
    navController: NavHostController = rememberNavController(),
    userDataStore: UserDataStore = UserDataStore(LocalContext.current),
    userRepository: UserRepository = UserRepository(userDataStore),
): MainAppState {
   return remember(navController) {
        MainAppState(navController, userRepository)
    }
}

class MainAppState(
    val navController: NavHostController,
    val userRepository: UserRepository
){
    val mainBottomNavBarTabs = MainBottomNavTabs.values()
    private val mainBottomNavBarRoutes = mainBottomNavBarTabs.map { it.route }

    val showBottomNavBar: Boolean
       @Composable get() = navController.currentBackStackEntryAsState().value?.destination?.route in mainBottomNavBarRoutes

    val currentRoute: String?
            get() = navController.currentDestination?.route

    fun navigateMainBottomNavBarTo(route: String){
        if(route != currentRoute) {
            navController.navigate(route) {
                if (!popUpToRoute.isNullOrEmpty()) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

    fun getUserAuthStatus(): Boolean {
        val auth = runBlocking {
            val userEmail = async { userRepository.getUserEmail() }
            val userPassword = async { userRepository.getUserPassword() }
            userEmail.await()!==null && userPassword.await()!==null
        }
        return auth
    }

    fun navigateTo(
        route: String,
        popUpToRoute: String? = null,
        inclusiveCurrent: Boolean = true,
    ){
        navController.navigate(route) {
            if(!popUpToRoute.isNullOrEmpty()) {
                popUpTo(popUpToRoute) { inclusive = inclusiveCurrent }
                launchSingleTop = true
            }
        }
    }

    fun navigateBack(){
        navController.popBackStack()
    }
}

