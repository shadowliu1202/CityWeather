package com.weather.feature.weather.presentation.weekly

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weather.core.model.CurrentWeather
import com.weather.core.model.DailyForecast
import com.weather.core.model.Weather
import com.weather.core.model.WeatherCondition
import com.weather.core.ui.theme.WeatherAccentBlue
import com.weather.core.ui.theme.WeatherTextPrimary
import java.time.Instant

@Composable
internal fun WeeklySection(weather: Weather) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "7-Day Forecast",
                color = WeatherTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Weekly",
                color = WeatherAccentBlue,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        WeeklyForecastSection(
            weeklyForecasts = weather.weeklyForecasts,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
    }
}

@Preview
@Composable
private fun PreviewWeeklySection() {
    WeeklySection(
        weather = Weather(
            CurrentWeather(
                cityName = "Sunnydale",
                date = Instant.now(),
                temperatureCelsius = 4427,
                condition = WeatherCondition.SUNNY,
                feelsLikeCelsius = 24,
                humidityPercent = 1458,
                windSpeedKmh = 4722,
            ),
            hourlyForecasts = listOf(),
            weeklyForecasts = listOf(
                DailyForecast(
                    dayOfWeek = "libris", highCelsius = 5878, lowCelsius = 9948, condition = WeatherCondition.SUNNY
                ),
                DailyForecast(
                    dayOfWeek = "libris", highCelsius = 5878, lowCelsius = 9948, condition = WeatherCondition.SUNNY
                )
            )
        )
    )
}