package com.weather.feature.weather.presentation.today

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weather.core.model.HourlyForecast
import com.weather.core.model.WeatherCondition
import com.weather.core.ui.theme.WeatherTextPrimary
import com.weather.core.ui.theme.WeatherTextSecondary
import com.weather.core.ui.theme.WeatherTheme
import com.weather.feature.weather.presentation.weatherIcon
import com.weather.feature.weather.presentation.weatherIconTint
import java.time.LocalTime

@Composable
internal fun HourlyForecastRow(
    hourlyForecasts: List<HourlyForecast>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 0.dp)
    ) {
        itemsIndexed(hourlyForecasts) { index, forecast ->
            HourlyForecastCard(
                forecast = forecast,
                isNow = index == 0
            )
        }
    }
}

@Composable
private fun HourlyForecastCard(
    forecast: HourlyForecast,
    isNow: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = forecast.formatTime(),
            color = if (isNow) WeatherTextPrimary else WeatherTextSecondary,
            fontSize = 12.sp,
            fontWeight = if (isNow) FontWeight.SemiBold else FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(8.dp))
        Icon(
            imageVector = forecast.condition.weatherIcon(),
            contentDescription = forecast.condition.label,
            tint = forecast.condition.weatherIconTint(),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "${forecast.temperatureCelsius}°",
            color = WeatherTextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
private fun PreviewHourlyForecastCard() {
    WeatherTheme {
        HourlyForecastCard(
            forecast = HourlyForecast(
                time = LocalTime.now(),
                temperatureCelsius = 22,
                condition = WeatherCondition.SUNNY
            ),
            isNow = true
        )
    }
}

@Preview
@Composable
private fun PreviewHourlyForecastRow() {
    WeatherTheme {
        HourlyForecastRow(
            hourlyForecasts = listOf(
                HourlyForecast(LocalTime.now(), 22, WeatherCondition.SUNNY),
                HourlyForecast(LocalTime.now().plusHours(1), 23, WeatherCondition.SUNNY),
                HourlyForecast(LocalTime.now().plusHours(2), 24, WeatherCondition.PARTLY_CLOUDY),
                HourlyForecast(LocalTime.now().plusHours(3), 24, WeatherCondition.CLOUDY),
                HourlyForecast(LocalTime.now().plusHours(4), 23, WeatherCondition.RAINY),
                HourlyForecast(LocalTime.now().plusHours(5), 22, WeatherCondition.RAINY),
            )
        )
    }
}
