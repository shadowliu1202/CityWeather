package com.weather.feature.weather.infra.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherDto(
    val name: String,
    val dt: Long,
    val main: CurrentMainDto,
    val weather: List<WeatherDescriptionDto>,
    val wind: WindDto
)

@Serializable
data class CurrentMainDto(
    val temp: Double,
    @SerialName("feels_like") val feelsLike: Double,
    val humidity: Int
)

@Serializable
data class WeatherDescriptionDto(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

@Serializable
data class WindDto(
    val speed: Double
)
