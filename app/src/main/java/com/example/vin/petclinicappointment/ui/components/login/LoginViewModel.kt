package com.example.vin.petclinicappointment.ui.components.login

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.example.vin.petclinicappointment.data.model.*
import com.example.vin.petclinicappointment.data.repository.UserRepository
import com.example.vin.petclinicappointment.ui.BaseViewModel
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
): BaseViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    private val _customer = MutableStateFlow<User?>(null)
    val customer = _customer.asStateFlow()

    private val _clinic = MutableStateFlow<PetClinic?>(null)
    val clinic = _clinic.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    fun setEmail(value: String){
        _email.value = value
    }

    fun setPassword(value: String){
        _password.value = value
    }

    private fun validateUser(user: User): Boolean {
        val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}\$"
        val passwordPattern = Pattern.compile(passwordRegex)
        if(user.email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(user.email).matches()){
            return false
        }
        if(user.password == null || !passwordPattern.matcher(user.password).matches()){
            return false
        }
        return true
    }

    suspend fun login() {
        if(validateUser(
                Customer(
                    email = email.value,
                    password = password.value
                )
        )) {
            val response = viewModelScope.async(Dispatchers.IO) {
                userRepository.login(LoginBody(
                    email = email.value,
                    password = password.value
                ))
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()?.data
                    if (data == null) {
                        setMessage(response.data?.body()?.status?.message as String)
                        return
                    }
                    _customer.value = data.customer.let { customer ->
                        Customer(
                            id = customer.id,
                            name = customer.name,
                            email = customer.email
                        )
                    }
                    userRepository.saveUserRole(data.role)
                    saveUserData(data.role)
                    if (data.status) {
                        _isLoggedIn.value = true
                    }
                }
                else -> setMessage(response.data?.message() as String)
            }
        }
    }

    suspend fun loginClinic() {
        if(validateUser(PetClinic(
                email = email.value,
                password = password.value
        ))) {
            val response = viewModelScope.async(Dispatchers.IO) {
                userRepository.loginClinic(LoginBody(
                    email = email.value,
                    password = password.value
                ))
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()?.data
                    if (data == null) {
                        setMessage(response.data?.body()?.status?.message as String)
                        return
                    }
                    _clinic.value = data.pet_clinic.let { clinic ->
                        PetClinic(
                            id = clinic.id,
                            name = clinic.name,
                            email = clinic.email,
                            image = clinic.image,
                            phoneNum = clinic.phoneNum,
                            address = clinic.address,
                            villageId = clinic.villageId,
                            latitude = clinic.latitude,
                            longitude = clinic.longitude
                        )
                    }
                    userRepository.saveUserRole(data.role)
                    saveUserData(data.role)
                    if (data.status) {
                        _isLoggedIn.value = true
                    }
                }
                else -> setMessage(response.data?.message() as String)
            }
        }
    }



    private suspend fun saveUserData(role: String) {
        if(role == "customer") {
            _customer.value?.let {
                userRepository.saveUserId(it.id ?: 0)
                userRepository.saveUserName(it.name ?: "")
                userRepository.saveUserEmail(it.email)
            }
            userRepository.saveUserPassword(password.value)
        }else if(role == "clinic"){
            _clinic.value?.let {
                userRepository.saveUserId(it.id ?: 0)
                userRepository.saveUserName(it.name ?: "")
                userRepository.saveUserEmail(it.email)
                userRepository.saveUserImage(it.image ?: "")
                userRepository.saveUserPhoneNum(it.phoneNum ?: "")
                userRepository.saveUserAddress(it.address ?: "")
                userRepository.saveUserVillageId(it.villageId ?: 0)
                userRepository.saveUserLatitude(it.latitude ?: 0.0)
                userRepository.saveUserLongitude(it.longitude ?: 0.0)
            }
        }
    }
}