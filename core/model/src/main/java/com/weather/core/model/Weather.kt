package com.weather.core.model

import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


data class Weather(
    val current: CurrentWeather,
    val hourlyForecasts: List<HourlyForecast>,
    val weeklyForecasts: List<DailyForecast>
)

data class CurrentWeather(
    val cityName: String,
    val date: Instant,
    val temperatureCelsius: Int,
    val condition: WeatherCondition,
    val feelsLikeCelsius: Int,
    val humidityPercent: Int,
    val windSpeedKmh: Int,
)

data class HourlyForecast(
    val time: LocalTime,
    val temperatureCelsius: Int,
    val condition: WeatherCondition
) {
    fun formatTime(): String {
        return time
            .format(DateTimeFormatter.ofPattern("HH:mm"))
    }
}

data class DailyForecast(
    val date: LocalDate,
    val highCelsius: Int,
    val lowCelsius: Int,
    val condition: WeatherCondition
) {
    val dayOfWeek = date.dayOfWeek
        .getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
        .uppercase()
    val isToday = date == LocalDate.now()
}

enum class WeatherCondition(val label: String) {
    SUNNY("Sunny"),
    CLOUDY("Cloudy"),
    PARTLY_CLOUDY("Partly Cloudy"),
    RAINY("Rainy"),
    STORMY("Stormy"),
    SNOWY("Snowy"),
    WINDY("Windy"),
    FOGGY("Foggy")
}
