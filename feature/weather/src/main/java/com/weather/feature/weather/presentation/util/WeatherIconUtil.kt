package com.weather.feature.weather.presentation.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.weather.core.model.WeatherCondition
import com.weather.core.ui.theme.WeatherAccentBlue
import com.weather.core.ui.theme.WeatherRainBlue
import com.weather.core.ui.theme.WeatherSunYellow
import com.weather.core.ui.theme.WeatherTextSecondary

internal fun WeatherCondition.weatherIcon(): ImageVector {
    return when (this) {
        WeatherCondition.SUNNY -> Icons.Default.WbSunny
        WeatherCondition.CLOUDY -> Icons.Default.Cloud
        WeatherCondition.PARTLY_CLOUDY -> Icons.Default.WbCloudy
        WeatherCondition.RAINY -> Icons.Default.Grain
        WeatherCondition.STORMY -> Icons.Default.Thunderstorm
        WeatherCondition.SNOWY -> Icons.Default.AcUnit
        WeatherCondition.WINDY -> Icons.Default.Air
        WeatherCondition.FOGGY -> Icons.Default.Cloud
    }
}

internal fun WeatherCondition.weatherIconTint(): Color {
    return when (this) {
        WeatherCondition.SUNNY -> WeatherSunYellow
        WeatherCondition.CLOUDY -> WeatherTextSecondary
        WeatherCondition.PARTLY_CLOUDY -> WeatherTextSecondary
        WeatherCondition.RAINY -> WeatherRainBlue
        WeatherCondition.STORMY -> WeatherAccentBlue
        WeatherCondition.SNOWY -> Color.White
        WeatherCondition.WINDY -> WeatherTextSecondary
        WeatherCondition.FOGGY -> WeatherTextSecondary
    }
}
