package com.weather.core.model

import kotlinx.serialization.Serializable

@Serializable
data class City(
    val id: String,
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double
) {
    companion object {
        val Default = City(
            id = "taipei",
            name = "Taipei",
            country = "Taiwan",
            latitude = 25.0330,
            longitude = 121.5654
        )
    }
}
