package com.example.vin.petclinicappointment.ui.components.profile

import androidx.lifecycle.viewModelScope
import com.example.vin.petclinicappointment.data.model.Call
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
class CustomerProfileViewModel @Inject constructor(
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

    suspend fun getCustomerDetail(){
        val userId = userId.value
        if(userId !== null) {
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

    suspend fun logout() = userRepository.logout()
}