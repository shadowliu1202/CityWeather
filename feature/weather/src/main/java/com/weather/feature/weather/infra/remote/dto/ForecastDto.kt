package com.weather.feature.weather.infra.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponseDto(
    val list: List<ForecastItemDto>,
    val city: ForecastCityDto
)

@Serializable
data class ForecastItemDto(
    val dt: Long,
    val main: ForecastMainDto,
    val weather: List<WeatherDescriptionDto>,
    @SerialName("dt_txt") val dtTxt: String
)

@Serializable
data class ForecastMainDto(
    val temp: Double,
    @SerialName("temp_min") val tempMin: Double,
    @SerialName("temp_max") val tempMax: Double,
    val humidity: Int = 0
)

@Serializable
data class ForecastCityDto(
    val name: String,
    val timezone: Int,
)
