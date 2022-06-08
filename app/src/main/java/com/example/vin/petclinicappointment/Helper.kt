package com.example.vin.petclinicappointment

fun <K,V> getKeyFromValue(map: Map<K, V>, value: V): K{
    return map.filter { value == it.value }.keys.first()
}