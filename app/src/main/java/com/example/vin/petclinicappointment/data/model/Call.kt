package com.example.vin.petclinicappointment.data.model

sealed class Call<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T): Call<T>(data)
    class Loading<T>: Call<T>()
    class Error<T>(message: String): Call<T>(message = message)
}
