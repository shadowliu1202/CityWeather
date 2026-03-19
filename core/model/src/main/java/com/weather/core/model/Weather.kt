package com.weather.core.model

import java.time.Instant


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
    val time: String,
    val temperatureCelsius: Int,
    val condition: WeatherCondition
)

data class DailyForecast(
    val dayOfWeek: String,
    val highCelsius: Int,
    val lowCelsius: Int,
    val condition: WeatherCondition
)

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
