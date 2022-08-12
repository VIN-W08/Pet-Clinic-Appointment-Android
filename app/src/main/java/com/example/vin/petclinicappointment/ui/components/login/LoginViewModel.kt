package com.example.vin.petclinicappointment.ui.components.login

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.example.vin.petclinicappointment.data.model.*
import com.example.vin.petclinicappointment.data.repository.CustomerRepository
import com.example.vin.petclinicappointment.data.repository.PetClinicRepository
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
    private val userRepository: UserRepository,
    private val customerRepository: CustomerRepository,
    private val petClinicRepository: PetClinicRepository
): BaseViewModel() {

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    fun setEmail(value: String) {
        _email.value = value
    }

    fun setPassword(value: String) {
        _password.value = value
    }

    private fun validateUser(): Boolean {
        val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}\$"
        val passwordPattern = Pattern.compile(passwordRegex)
        if (email.value.trim().isEmpty()) {
            setMessage("Email wajib diinput")
            return false
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email.value.trim()).matches()){
            setMessage(("Format email tidak valid"))
            return false
        }
        if(password.value.trim().isEmpty()){
            setMessage("Kata sandi wajib diinput")
            return false
        }
        if(!passwordPattern.matcher(password.value.trim()).matches()){
            setMessage("Kata sandi harus memiliki minimal 8 karakter yang terdiri huruf besar, huruf kecil, dan angka")
            return false
        }
        return true
    }

    suspend fun login(role: String): Boolean {
        if (validateUser()) {
            val loginBody = LoginBody(email = email.value, password = password.value)
            when (role) {
                "customer" -> {
                    val response = viewModelScope.async(Dispatchers.IO) {
                        customerRepository.loginCustomer(loginBody)
                    }.await()
                    when (response) {
                        is Call.Success -> {
                            val data = response.data?.body()?.data
                            if (data == null) {
                                setMessage(response.data?.body()?.status?.message as String)
                                return false
                            }
                            saveUserData(data.customer)
                            userRepository.saveUserRole(data.role)
                            return data.status
                        }
                        else -> setMessage(response.data?.message() as String)
                    }
                }
                "clinic" -> {
                    val response = viewModelScope.async(Dispatchers.IO) {
                        petClinicRepository.loginClinic(loginBody)
                    }.await()
                    when (response) {
                        is Call.Success -> {
                            val data = response.data?.body()?.data
                            if (data == null) {
                                setMessage(response.data?.body()?.status?.message as String)
                                return false
                            }
                            saveUserData(data.pet_clinic)
                            userRepository.saveUserRole(data.role)
                            return data.status
                        }
                        else -> throw IllegalArgumentException()
                    }
                }
            }
        }
        return false
    }

    private suspend fun saveUserData(user: User) {
        userRepository.saveUserId(user.id ?: 0)
        userRepository.saveUserName(user.name ?: "")
        userRepository.saveUserEmail(user.email)
        userRepository.saveUserPassword(user.password ?: "")
    }

}
