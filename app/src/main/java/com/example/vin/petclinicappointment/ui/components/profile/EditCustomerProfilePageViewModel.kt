package com.example.vin.petclinicappointment.ui.components.profile

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
import javax.inject.Inject

@HiltViewModel
class EditCustomerProfilePageViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val userRepository: UserRepository
): BaseViewModel() {

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
            return false
        }
        if(name.value.trim().isEmpty()){
            return false
        }
        return true
    }

    suspend fun getUserData(){
        userRepository.let {
            _name.value = it.getUserName() ?: ""
            _email.value = it.getUserEmail() ?: ""
        }
    }

    suspend fun updateCustomer(): Boolean {
        if(validateCustomer()) {
            val userId = userRepository.getUserId()
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

    suspend fun updateLocalCustomer(){
        userRepository.saveUserName(name.value)
        userRepository.saveUserEmail(email.value)
        userRepository.saveUserRole("")
    }
}