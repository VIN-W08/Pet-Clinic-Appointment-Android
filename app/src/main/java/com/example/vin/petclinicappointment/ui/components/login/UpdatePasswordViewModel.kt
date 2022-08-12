package com.example.vin.petclinicappointment.ui.components.login

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.example.vin.petclinicappointment.data.model.Call
import com.example.vin.petclinicappointment.data.model.UpdatePasswordBody
import com.example.vin.petclinicappointment.data.repository.CustomerRepository
import com.example.vin.petclinicappointment.data.repository.PetClinicRepository
import com.example.vin.petclinicappointment.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class UpdatePasswordViewModel @Inject constructor(
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

    suspend fun updatePassword(role: String): Boolean {
        if (validateUser()) {
            val body = UpdatePasswordBody(email = email.value, password = password.value)
            val response = viewModelScope.async(Dispatchers.IO) {
                when (role) {
                    "customer" -> customerRepository.updatePassword(body)
                    "clinic" -> petClinicRepository.updatePassword(body)
                    else -> throw IllegalArgumentException()
                }
            }.await()
            return when (response) {
                is Call.Success -> {
                    val data = response.data?.body()?.data
                    if (data == null) {
                        setMessage(response.data?.body()?.status?.message as String)
                    }
                    data !== null
                }
                else -> {
                    setMessage(response.data?.message() as String)
                    return false
                }
            }
        }
        return false
    }
}