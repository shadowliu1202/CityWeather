package com.weather.feature.weather.infra.adapter

import com.weather.core.model.CurrentWeather
import com.weather.core.model.DailyForecast
import com.weather.core.model.HourlyForecast
import com.weather.core.model.Weather
import com.weather.core.model.WeatherCondition
import com.weather.feature.weather.infra.remote.dto.CurrentWeatherDto
import com.weather.feature.weather.infra.remote.dto.ForecastCityDto
import com.weather.feature.weather.infra.remote.dto.ForecastItemDto
import com.weather.feature.weather.infra.remote.dto.ForecastResponseDto
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

internal class Mapper {
    fun mapToWeather(
        current: CurrentWeatherDto,
        forecast: ForecastResponseDto
    ): Weather {
        return Weather(
            current = mapCurrentWeather(current),
            hourlyForecasts = mapHourly(forecast.city, forecast.list),
            weeklyForecasts = mapWeekly(forecast.city, forecast.list)
        )
    }

    private fun mapCurrentWeather(current: CurrentWeatherDto): CurrentWeather {
        return CurrentWeather(
            cityName = current.name,
            date = Instant.ofEpochSecond(current.dt),
            temperatureCelsius = current.main.temp.toInt(),
            condition = mapCondition(current.weather.firstOrNull()?.id ?: 800),
            feelsLikeCelsius = current.main.feelsLike.toInt(),
            humidityPercent = current.main.humidity,
            // OpenWeatherMap returns wind speed in m/s → convert to km/h
            windSpeedKmh = (current.wind.speed * 3.6).toInt(),
        )
    }

    private fun mapHourly(city: ForecastCityDto, items: List<ForecastItemDto>): List<HourlyForecast> =
        items.mapIndexed { _, item ->
            HourlyForecast(
                time = formatToLocalDateTime(item.dtTxt, city.timezone).toLocalTime(),
                temperatureCelsius = item.main.temp.toInt(),
                condition = mapCondition(item.weather.firstOrNull()?.id ?: 800)
            )
        }

    private fun mapWeekly(city: ForecastCityDto, items: List<ForecastItemDto>): List<DailyForecast> {
        val grouped = items.groupBy {
            formatToLocalDateTime(it.dtTxt, city.timezone).toLocalDate()
        }
        return grouped.entries.take(7).map { (date, dayItems) ->
            val representative = dayItems.minByOrNull { item ->
                formatToLocalDateTime(item.dtTxt, city.timezone).hour
            } ?: dayItems.first()
            DailyForecast(
                date = date,
                highCelsius = dayItems.maxOf { it.main.tempMax }.toInt(),
                lowCelsius = dayItems.minOf { it.main.tempMin }.toInt(),
                condition = mapCondition(representative.weather.firstOrNull()?.id ?: 800)
            )
        }
    }

    // ── Weather condition mapping ─────────────────────────────────────────────

    /**
     * Maps OpenWeatherMap weather condition IDs to [com.weather.core.model.WeatherCondition].
     * Full ID list: https://openweathermap.org/weather-conditions
     */
    private fun mapCondition(id: Int): WeatherCondition = when (id) {
        in 200..299 -> WeatherCondition.STORMY        // Thunderstorm
        in 300..399 -> WeatherCondition.RAINY         // Drizzle
        in 500..599 -> WeatherCondition.RAINY         // Rain
        in 600..699 -> WeatherCondition.SNOWY         // Snow
        in 700..799 -> WeatherCondition.FOGGY         // Atmosphere (mist, fog, haze…)
        800 -> WeatherCondition.SUNNY          // Clear sky
        801, 802 -> WeatherCondition.PARTLY_CLOUDY // 11–50 % clouds
        in 803..804 -> WeatherCondition.CLOUDY        // 51–100 % clouds
        else -> WeatherCondition.CLOUDY
    }

    private fun formatToLocalDateTime(time: String, offset: Int): LocalDateTime {
        val utcDateTime = LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val zoneOffset = ZoneOffset.ofTotalSeconds(offset)
        return utcDateTime.atOffset(ZoneOffset.UTC)
            .withOffsetSameInstant(zoneOffset)
            .toLocalDateTime()
    }
}