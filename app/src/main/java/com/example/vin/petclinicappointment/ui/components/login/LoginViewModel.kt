package com.example.vin.petclinicappointment.ui.components.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vin.petclinicappointment.data.model.Call
import com.example.vin.petclinicappointment.data.model.User
import com.example.vin.petclinicappointment.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor (
    private val userRepository: UserRepository
): ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    private val _hasLoggedIn = MutableStateFlow(false)
    val hasLoggedIn = _hasLoggedIn.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user = _user

    private fun validateUser(user: User): Boolean {
        val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}\$"
        val passwordPattern = Pattern.compile(passwordRegex)
        if(user.email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(user.email).matches()){
            return false
        }
        if(user.password.isEmpty() && !passwordPattern.matcher(user.password).matches()){
            return false
        }
        return true
    }

    suspend fun login(user: User) {
        if(validateUser(user)) {
            val response = viewModelScope.async(Dispatchers.IO) {
                userRepository.login(User(user.email, user.password))
            }.await()
            when (response) {
                is Call.Success -> {
                    _user.value = response.data
                    _isLoggedIn.value = true
                    saveUserData()
                }
                else -> {}
            }
        }
    }

    private suspend fun saveUserData() {
        _user.value?.let { userRepository.saveUserName(it.name?:"") }
        _user.value?.let { userRepository.saveUserEmail(it.email) }
        _user.value?.let { userRepository.saveUserPassword(it.password) }
        _user.value?.let { userRepository.saveUserAddress(it.address?:"") }
    }
}