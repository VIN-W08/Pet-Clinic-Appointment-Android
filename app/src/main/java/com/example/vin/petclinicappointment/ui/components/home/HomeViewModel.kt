package com.example.vin.petclinicappointment.ui.components.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vin.petclinicappointment.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    suspend fun getUserData() {
        viewModelScope.launch {
            _username.value = userRepository.getUserName() ?: ""
            _email.value = userRepository.getUserEmail() ?: ""
        }
    }
}