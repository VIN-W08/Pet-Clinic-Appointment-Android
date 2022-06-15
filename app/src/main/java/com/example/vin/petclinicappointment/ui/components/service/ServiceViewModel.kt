package com.example.vin.petclinicappointment.ui.components.service

import androidx.lifecycle.viewModelScope
import com.example.vin.petclinicappointment.data.model.Call
import com.example.vin.petclinicappointment.data.model.ResponseStatus
import com.example.vin.petclinicappointment.data.model.Service
import com.example.vin.petclinicappointment.data.repository.ServiceRepository
import com.example.vin.petclinicappointment.data.repository.UserRepository
import com.example.vin.petclinicappointment.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject
import retrofit2.Response
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class ServiceViewModel @Inject constructor(
    private val serviceRepository: ServiceRepository,
    private val userRepository: UserRepository
): BaseViewModel() {
    private val _serviceList = MutableStateFlow<List<Service>>(listOf())
    val serviceList = _serviceList.asStateFlow()

    suspend fun getServiceList(){
        val userId = userRepository.getUserId()
        if(userId !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                serviceRepository.getServiceList(
                    clinicId = userId,
                    name = ""
                )
            }.await()
            when (response) {
                is Call.Success -> {
                    var data = response.data?.body()?.data
                    if(data !== null) {
                        data = data.map {
                            it.price = DecimalFormat("0.#").format(it.price).toFloat()
                            it
                        }
                        _serviceList.value = data
                    }
                }
                else -> {
                    val errorJson = JSONObject((response.data as Response<ResponseStatus>).errorBody()?.string())
                    setMessage(errorJson.getJSONObject("status").getString("message"))
                }
            }
        }
    }
}