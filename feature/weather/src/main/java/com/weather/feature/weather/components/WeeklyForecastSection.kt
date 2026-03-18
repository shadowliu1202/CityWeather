package com.weather.feature.weather.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weather.core.model.DailyForecast
import com.weather.core.ui.theme.WeatherCardBackground
import com.weather.core.ui.theme.WeatherCardSelected
import com.weather.core.ui.theme.WeatherTextPrimary
import com.weather.core.ui.theme.WeatherTextSecondary
import com.weather.feature.weather.util.weatherIcon
import com.weather.feature.weather.util.weatherIconTint

@Composable
fun WeeklyForecastSection(
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
                isToday = forecast.dayOfWeek == "TODAY"
            )
        }
    }
}

@Composable
fun DailyForecastCard(
    forecast: DailyForecast,
    isToday: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.width(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isToday) WeatherCardSelected else WeatherCardBackground
        )
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
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
            Text(
                text = "${forecast.highCelsius}°",
                color = WeatherTextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${forecast.lowCelsius}°",
                color = WeatherTextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}
