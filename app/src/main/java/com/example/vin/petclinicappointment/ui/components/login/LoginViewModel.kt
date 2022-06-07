package com.example.vin.petclinicappointment.ui.components.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.example.vin.petclinicappointment.data.model.Call
import com.example.vin.petclinicappointment.data.model.Customer
import com.example.vin.petclinicappointment.data.model.LoginBody
import com.example.vin.petclinicappointment.data.model.User
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
                userRepository.login(LoginBody(
                    email = user.email,
                    password = user.password
                ))
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()?.data
                    if(data == null){
                        setMessage(response.data?.body()?.status?.message as String)
                        return
                    }
                    if(data.role == "customer") {
                        _customer.value = data.customer.let {
                            Log.d("debug1", "user id in func:${it.id}")
                            Customer(
                                id = it.id,
                                name = it.name,
                                email = it.email,
                                password = it.password
                            )
                        }
                        saveCustomerData()
                    }
                    if(data.status) {
                        _isLoggedIn.value = true
                    }
                }
                else -> setMessage(response.data?.message() as String)
            }
        }
    }

    private suspend fun saveCustomerData() {
        Log.d("debug1", "customer email:${customer.value?.email}")
        Log.d("debug1", "customer password:${customer.value?.password}")
        Log.d("debug1", "customer name:${customer.value?.name}")
        Log.d("debug1", "customer id:${customer.value?.id}")
        _customer.value?.let { userRepository.saveUserId(it.id ?: 0)}
        _customer.value?.let { userRepository.saveUserName(it.name?:"") }
        _customer.value?.let { userRepository.saveUserEmail(it.email) }
        _customer.value?.let { userRepository.saveUserPassword(it.password) }
    }
}