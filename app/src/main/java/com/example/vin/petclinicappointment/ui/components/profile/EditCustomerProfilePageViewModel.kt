package com.example.vin.petclinicappointment.ui.components.profile

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.example.vin.petclinicappointment.data.model.Call
import com.example.vin.petclinicappointment.data.model.UpdateCustomerBody
import com.example.vin.petclinicappointment.data.repository.CustomerRepository
import com.example.vin.petclinicappointment.data.repository.UserRepository
import com.example.vin.petclinicappointment.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditCustomerProfilePageViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val userRepository: UserRepository
): BaseViewModel() {
    private val _userId = MutableStateFlow<Int?>(null)
    val userId = _userId.asStateFlow()

    init {
        viewModelScope.launch {
            _userId.value = userRepository.getUserId()
        }
    }

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    fun setName(value: String){
        _name.value = value
    }

    fun setEmail(value: String){
        _email.value = value
    }

    private fun validateCustomer(): Boolean {
        if(name.value.trim().isEmpty()){
            setMessage("Nama wajib diinput")
            return false
        }
        if(email.value.trim().isEmpty()){
            setMessage("Email wajib diinput")
            return false
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email.value.trim()).matches()){
            setMessage("Format email tidak valid")
            return false
        }
        return true
    }

    suspend fun getCustomerDetail(){
        val userId = userId.value
        if (userId !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                customerRepository.getCustomerDetail(userId)
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()?.data
                    if (data !== null) {
                        _name.value = data.name ?: ""
                        _email.value = data.email
                    } else {
                        setMessage(response.data?.body()?.status?.message as String)
                    }
                }
                else -> setMessage(response.data?.message() as String)
            }
        }
    }

    suspend fun updateCustomer(): Boolean {
        if(validateCustomer()) {
            val userId = userId.value
            if (userId !== null) {
                val body = UpdateCustomerBody(
                    name = name.value,
                    email = email.value
                )
                val response = viewModelScope.async(Dispatchers.IO) {
                    customerRepository.updateCustomer(userId, body)
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
        return false
    }

    suspend fun updateLocalUser(){
        userRepository.saveUserName(name.value)
        userRepository.saveUserEmail(email.value)
    }
}