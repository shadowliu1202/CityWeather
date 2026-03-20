package com.weather.feature.weather.presentation.weekly

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weather.core.model.DailyForecast
import com.weather.core.model.WeatherCondition
import com.weather.core.ui.theme.WeatherTextPrimary
import com.weather.core.ui.theme.WeatherTextSecondary
import com.weather.feature.weather.presentation.weatherIcon
import com.weather.feature.weather.presentation.weatherIconTint
import java.time.LocalDate

@Composable
internal fun WeeklyForecastSection(
    weeklyForecasts: List<DailyForecast>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 0.dp)
    ) {
        items(weeklyForecasts) { forecast ->
            DailyForecastCard(
                forecast = forecast,
                isToday = forecast.isToday
            )
        }
    }
}

@Composable
private fun DailyForecastCard(
    forecast: DailyForecast,
    isToday: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = forecast.dayOfWeek,
            color = if (isToday) WeatherTextPrimary else WeatherTextSecondary,
            fontSize = 11.sp,
            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(8.dp))
        Icon(
            imageVector = forecast.condition.weatherIcon(),
            contentDescription = forecast.condition.label,
            tint = forecast.condition.weatherIconTint(),
            modifier = Modifier.size(26.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row() {
            Text(
                text = "${forecast.lowCelsius}°",
                color = WeatherTextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = "/",
                color = WeatherTextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${forecast.highCelsius}°",
                color = WeatherTextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Preview
@Composable
private fun PreviewDailyForecastCard() {
    Row {
        DailyForecastCard(
            forecast = DailyForecast(
                date = LocalDate.now(),
                highCelsius = 27,
                lowCelsius = 73,
                condition = WeatherCondition.SUNNY
            ), isToday = true
        )
        DailyForecastCard(
            forecast = DailyForecast(
                date = LocalDate.now(),
                highCelsius = 25,
                lowCelsius = 98,
                condition = WeatherCondition.SUNNY
            ), isToday = false
        )
    }

}