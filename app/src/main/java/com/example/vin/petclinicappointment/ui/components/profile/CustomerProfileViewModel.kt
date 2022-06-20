package com.example.vin.petclinicappointment.ui.components.profile

import com.example.vin.petclinicappointment.data.repository.UserRepository
import com.example.vin.petclinicappointment.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CustomerProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
): BaseViewModel() {
    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    suspend fun getUserData(){
        userRepository.let {
            _name.value = it.getUserName() ?: ""
            _email.value = it.getUserEmail() ?: ""
        }
    }

    suspend fun logout(){
        val userRole = userRepository.getUserRole()
        if (userRole != null) {
            userRepository.logout(userRole)
        }
    }
}