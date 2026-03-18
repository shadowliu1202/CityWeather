package com.weather.core.model

data class Weather(
    val cityName: String,
    val date: String,
    val temperatureCelsius: Int,
    val condition: WeatherCondition,
    val feelsLikeCelsius: Int,
    val humidityPercent: Int,
    val windSpeedKmh: Int,
    val hourlyForecasts: List<HourlyForecast>,
    val weeklyForecasts: List<DailyForecast>
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
