package com.weather.feature.weather.infra.remote

import com.weather.core.model.DailyForecast
import com.weather.core.model.HourlyForecast
import com.weather.core.model.Weather
import com.weather.core.model.WeatherCondition
import com.weather.feature.weather.infra.remote.dto.CurrentWeatherDto
import com.weather.feature.weather.infra.remote.dto.ForecastItemDto
import com.weather.feature.weather.infra.remote.dto.ForecastResponseDto
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

internal class Mapper {
    fun mapToWeather(
        current: CurrentWeatherDto,
        forecast: ForecastResponseDto
    ): Weather {
        return Weather(
            cityName = current.name,
            date = formatDate(current.dt),
            temperatureCelsius = current.main.temp.toInt(),
            condition = mapCondition(current.weather.firstOrNull()?.id ?: 800),
            feelsLikeCelsius = current.main.feelsLike.toInt(),
            humidityPercent = current.main.humidity,
            // OpenWeatherMap returns wind speed in m/s → convert to km/h
            windSpeedKmh = (current.wind.speed * 3.6).toInt(),
            hourlyForecasts = mapHourly(forecast.list.take(8)),
            weeklyForecasts = mapWeekly(forecast.list)
        )
    }

    private fun mapHourly(items: List<ForecastItemDto>): List<HourlyForecast> =
        items.mapIndexed { index, item ->
            HourlyForecast(
                time = if (index == 0) "Now" else formatHour(item.dtTxt),
                temperatureCelsius = item.main.temp.toInt(),
                condition = mapCondition(item.weather.firstOrNull()?.id ?: 800)
            )
        }

    /**
     * Groups forecast items by calendar date, takes up to 7 days.
     * For each day, the entry closest to noon is used for the condition icon;
     * daily high/low are derived from all entries in that day.
     */
    private fun mapWeekly(items: List<ForecastItemDto>): List<DailyForecast> {
        val grouped = items.groupBy { it.dtTxt.take(10) } // "yyyy-MM-dd"
        return grouped.entries.take(7).mapIndexed { index, (_, dayItems) ->
            val representative = dayItems.minByOrNull { item ->
                val hour = item.dtTxt.substring(11, 13).toInt()
                kotlin.math.abs(hour - 12)
            } ?: dayItems.first()

            DailyForecast(
                dayOfWeek = if (index == 0) "TODAY" else formatDayLabel(representative.dtTxt.take(10)),
                highCelsius = dayItems.maxOf { it.main.tempMax }.toInt(),
                lowCelsius = dayItems.minOf { it.main.tempMin }.toInt(),
                condition = mapCondition(representative.weather.firstOrNull()?.id ?: 800)
            )
        }
    }

    // ── Weather condition mapping ─────────────────────────────────────────────

    /**
     * Maps OpenWeatherMap weather condition IDs to [WeatherCondition].
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

    // ── Formatting helpers ────────────────────────────────────────────────────

    /** Unix epoch seconds → "Monday, 12 Oct" */
    private fun formatDate(unixSeconds: Long): String {
        val formatter = DateTimeFormatter.ofPattern("EEEE, d MMM", Locale.ENGLISH)
        return Instant.ofEpochSecond(unixSeconds)
            .atZone(ZoneId.systemDefault())
            .format(formatter)
    }

    /** "2024-10-12 14:00:00" → "14:00" */
    private fun formatHour(dtTxt: String): String = dtTxt.substring(11, 16)

    /** "2024-10-12" → "MON", "TUE", … */
    private fun formatDayLabel(dateStr: String): String =
        LocalDate.parse(dateStr)
            .dayOfWeek
            .getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
            .uppercase()
}