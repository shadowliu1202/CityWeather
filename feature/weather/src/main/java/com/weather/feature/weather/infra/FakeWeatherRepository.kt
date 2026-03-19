package com.weather.feature.weather.infra

import com.weather.core.model.DailyForecast
import com.weather.core.model.HourlyForecast
import com.weather.core.model.Weather
import com.weather.core.model.WeatherCondition
import com.weather.feature.weather.domain.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FakeWeatherRepository @Inject constructor() : WeatherRepository {

    private val taipeiWeather = Weather(
        cityName = "Taipei",
        date = "Wednesday, Mar 18",
        temperatureCelsius = 28,
        condition = WeatherCondition.SUNNY,
        feelsLikeCelsius = 30,
        humidityPercent = 64,
        windSpeedKmh = 12,
        hourlyForecasts = listOf(
            HourlyForecast("Now", 28, WeatherCondition.SUNNY),
            HourlyForecast("14:00", 29, WeatherCondition.PARTLY_CLOUDY),
            HourlyForecast("15:00", 28, WeatherCondition.CLOUDY),
            HourlyForecast("16:00", 27, WeatherCondition.CLOUDY),
            HourlyForecast("17:00", 25, WeatherCondition.RAINY),
            HourlyForecast("18:00", 24, WeatherCondition.RAINY),
            HourlyForecast("19:00", 23, WeatherCondition.CLOUDY),
            HourlyForecast("20:00", 22, WeatherCondition.PARTLY_CLOUDY)
        ),
        weeklyForecasts = listOf(
            DailyForecast("TODAY", 28, 18, WeatherCondition.SUNNY),
            DailyForecast("MON", 26, 17, WeatherCondition.PARTLY_CLOUDY),
            DailyForecast("TUE", 23, 16, WeatherCondition.PARTLY_CLOUDY),
            DailyForecast("WED", 21, 15, WeatherCondition.RAINY),
            DailyForecast("THU", 19, 14, WeatherCondition.RAINY),
            DailyForecast("FRI", 20, 14, WeatherCondition.RAINY),
            DailyForecast("SAT", 25, 16, WeatherCondition.SUNNY)
        )
    )

    private val tokyoWeather = Weather(
        cityName = "Tokyo",
        date = "Wednesday, Mar 18",
        temperatureCelsius = 15,
        condition = WeatherCondition.PARTLY_CLOUDY,
        feelsLikeCelsius = 13,
        humidityPercent = 70,
        windSpeedKmh = 18,
        hourlyForecasts = listOf(
            HourlyForecast("Now", 15, WeatherCondition.PARTLY_CLOUDY),
            HourlyForecast("14:00", 16, WeatherCondition.CLOUDY),
            HourlyForecast("15:00", 16, WeatherCondition.CLOUDY),
            HourlyForecast("16:00", 15, WeatherCondition.RAINY),
            HourlyForecast("17:00", 14, WeatherCondition.RAINY),
            HourlyForecast("18:00", 13, WeatherCondition.RAINY),
            HourlyForecast("19:00", 12, WeatherCondition.CLOUDY),
            HourlyForecast("20:00", 11, WeatherCondition.PARTLY_CLOUDY)
        ),
        weeklyForecasts = listOf(
            DailyForecast("TODAY", 15, 8, WeatherCondition.PARTLY_CLOUDY),
            DailyForecast("MON", 17, 10, WeatherCondition.CLOUDY),
            DailyForecast("TUE", 14, 9, WeatherCondition.RAINY),
            DailyForecast("WED", 12, 7, WeatherCondition.RAINY),
            DailyForecast("THU", 13, 8, WeatherCondition.PARTLY_CLOUDY),
            DailyForecast("FRI", 16, 10, WeatherCondition.SUNNY),
            DailyForecast("SAT", 18, 11, WeatherCondition.SUNNY)
        )
    )

    private val newYorkWeather = Weather(
        cityName = "New York",
        date = "Wednesday, Mar 18",
        temperatureCelsius = 8,
        condition = WeatherCondition.CLOUDY,
        feelsLikeCelsius = 5,
        humidityPercent = 75,
        windSpeedKmh = 22,
        hourlyForecasts = listOf(
            HourlyForecast("Now", 8, WeatherCondition.CLOUDY),
            HourlyForecast("14:00", 9, WeatherCondition.CLOUDY),
            HourlyForecast("15:00", 9, WeatherCondition.PARTLY_CLOUDY),
            HourlyForecast("16:00", 8, WeatherCondition.CLOUDY),
            HourlyForecast("17:00", 7, WeatherCondition.RAINY),
            HourlyForecast("18:00", 6, WeatherCondition.RAINY),
            HourlyForecast("19:00", 6, WeatherCondition.CLOUDY),
            HourlyForecast("20:00", 5, WeatherCondition.CLOUDY)
        ),
        weeklyForecasts = listOf(
            DailyForecast("TODAY", 8, 2, WeatherCondition.CLOUDY),
            DailyForecast("MON", 10, 4, WeatherCondition.PARTLY_CLOUDY),
            DailyForecast("TUE", 12, 5, WeatherCondition.SUNNY),
            DailyForecast("WED", 11, 4, WeatherCondition.PARTLY_CLOUDY),
            DailyForecast("THU", 9, 3, WeatherCondition.RAINY),
            DailyForecast("FRI", 7, 1, WeatherCondition.SNOWY),
            DailyForecast("SAT", 6, 0, WeatherCondition.SNOWY)
        )
    )

    private val defaultWeather = taipeiWeather

    override fun getWeather(cityId: String): Flow<Weather> {
        val weather = when (cityId.lowercase()) {
            "taipei" -> taipeiWeather
            "tokyo" -> tokyoWeather
            "new_york", "newyork", "new york" -> newYorkWeather
            "london" -> Weather(
                cityName = "London",
                date = "Wednesday, Mar 18",
                temperatureCelsius = 12,
                condition = WeatherCondition.RAINY,
                feelsLikeCelsius = 9,
                humidityPercent = 85,
                windSpeedKmh = 25,
                hourlyForecasts = listOf(
                    HourlyForecast("Now", 12, WeatherCondition.RAINY),
                    HourlyForecast("14:00", 12, WeatherCondition.RAINY),
                    HourlyForecast("15:00", 11, WeatherCondition.CLOUDY),
                    HourlyForecast("16:00", 11, WeatherCondition.CLOUDY),
                    HourlyForecast("17:00", 10, WeatherCondition.RAINY),
                    HourlyForecast("18:00", 10, WeatherCondition.RAINY),
                    HourlyForecast("19:00", 9, WeatherCondition.CLOUDY),
                    HourlyForecast("20:00", 9, WeatherCondition.CLOUDY)
                ),
                weeklyForecasts = listOf(
                    DailyForecast("TODAY", 12, 7, WeatherCondition.RAINY),
                    DailyForecast("MON", 13, 8, WeatherCondition.CLOUDY),
                    DailyForecast("TUE", 14, 9, WeatherCondition.PARTLY_CLOUDY),
                    DailyForecast("WED", 12, 7, WeatherCondition.RAINY),
                    DailyForecast("THU", 11, 6, WeatherCondition.RAINY),
                    DailyForecast("FRI", 13, 8, WeatherCondition.CLOUDY),
                    DailyForecast("SAT", 15, 10, WeatherCondition.PARTLY_CLOUDY)
                )
            )
            else -> defaultWeather.copy(cityName = cityId.replaceFirstChar { it.uppercaseChar() })
        }
        return flowOf(weather)
    }
}