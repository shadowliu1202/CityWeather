package com.weather.feature.weather.presentation.today

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.DeviceThermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weather.core.model.CurrentWeather
import com.weather.core.model.WeatherCondition
import com.weather.core.ui.theme.WeatherAccentBlue
import com.weather.core.ui.theme.WeatherCardBackground
import com.weather.core.ui.theme.WeatherTextPrimary
import com.weather.core.ui.theme.WeatherTextSecondary
import com.weather.core.ui.theme.WeatherTheme
import java.time.Instant

@Composable
internal fun WeatherStatsRow(
    weather: CurrentWeather,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        WeatherStatCard(
            modifier = Modifier.weight(1f),
            icon = {
                Icon(
                    imageVector = Icons.Default.DeviceThermostat,
                    contentDescription = "Feels Like",
                    tint = WeatherAccentBlue,
                    modifier = Modifier.size(22.dp)
                )
            },
            label = "Feels Like",
            value = "${weather.feelsLikeCelsius}°C"
        )
        WeatherStatCard(
            modifier = Modifier.weight(1f),
            icon = {
                Icon(
                    imageVector = Icons.Default.WaterDrop,
                    contentDescription = "Humidity",
                    tint = WeatherAccentBlue,
                    modifier = Modifier.size(22.dp)
                )
            },
            label = "Humidity",
            value = "${weather.humidityPercent}%"
        )
        WeatherStatCard(
            modifier = Modifier.weight(1f),
            icon = {
                Icon(
                    imageVector = Icons.Default.Air,
                    contentDescription = "Wind",
                    tint = WeatherAccentBlue,
                    modifier = Modifier.size(22.dp)
                )
            },
            label = "Wind",
            value = "${weather.windSpeedKmh} km/h"
        )
    }
}

@Composable
fun WeatherStatCard(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    label: String,
    value: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = WeatherCardBackground
        )
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 14.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            icon()
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                color = WeatherTextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                color = WeatherTextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview
@Composable
private fun WeatherStatCardPreview() {
    WeatherTheme {
        WeatherStatCard(
            icon = {
                Icon(
                    imageVector = Icons.Default.DeviceThermostat,
                    contentDescription = "Feels Like",
                    tint = WeatherAccentBlue,
                    modifier = Modifier.size(22.dp)
                )
            },
            label = "Feels Like",
            value = "24°C"
        )
    }
}

@Preview
@Composable
private fun WeatherStatsRowPreview() {
    WeatherTheme {
        val sampleWeather = CurrentWeather(
            cityName = "San Francisco",
            date = Instant.now(),
            temperatureCelsius = 22,
            condition = WeatherCondition.SUNNY,
            feelsLikeCelsius = 24,
            humidityPercent = 65,
            windSpeedKmh = 10,
        )
        WeatherStatsRow(weather = sampleWeather)
    }
}
