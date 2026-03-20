package com.weather.feature.weather.presentation

import android.net.Uri
import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.weather.core.model.City
import com.weather.core.model.CurrentWeather
import com.weather.core.model.DailyForecast
import com.weather.core.model.HourlyForecast
import com.weather.core.model.Weather
import com.weather.core.model.WeatherCondition
import com.weather.core.ui.theme.WeatherAccentBlue
import com.weather.core.ui.theme.WeatherCardBackground
import com.weather.core.ui.theme.WeatherDarkBackground
import com.weather.core.ui.theme.WeatherTextPrimary
import com.weather.core.ui.theme.WeatherTextSecondary
import com.weather.core.ui.theme.WeatherTheme
import com.weather.feature.weather.presentation.today.HourlySection
import com.weather.feature.weather.presentation.today.WeatherStatsRow
import com.weather.feature.weather.presentation.weekly.WeeklySection
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.reflect.typeOf


private val CityNavType = object : NavType<City>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): City? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): City {
        return Json.decodeFromString(Uri.decode(value))
    }

    override fun serializeAsValue(value: City): String {
        return Uri.encode(Json.encodeToString(value))
    }

    override fun put(bundle: Bundle, key: String, value: City) {
        bundle.putString(key, Json.encodeToString(value))
    }
}

fun NavGraphBuilder.weatherScreen(
    onNavigateToCitySearch: () -> Unit,
) {
    composable<WeatherRoute>(
        typeMap = mapOf(typeOf<City>() to CityNavType)
    ) { backStackEntry ->
        val route: WeatherRoute = backStackEntry.toRoute()
        WeatherScreen(city = route.city, onNavigateToCitySearch = onNavigateToCitySearch)
    }
}

@Serializable
data class WeatherRoute(val city: City)

@Composable
private fun WeatherScreen(
    city: City,
    onNavigateToCitySearch: () -> Unit,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    LaunchedEffect(city) {
        viewModel.loadWeather(city)
    }

    val weatherState by viewModel.weatherState.collectAsState()

    WeatherScreen(
        city = city,
        weatherState = weatherState,
        onNavigateToCitySearch = onNavigateToCitySearch
    )
}

@Composable
private fun WeatherScreen(
    city: City,
    weatherState: WeatherUiState,
    onNavigateToCitySearch: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WeatherDarkBackground)
    ) {
        when (weatherState) {
            is WeatherUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = WeatherAccentBlue
                )
            }

            is WeatherUiState.Error -> {
                Text(
                    text = "Error: ${weatherState.message}",
                    color = WeatherTextPrimary,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }

            is WeatherUiState.Success -> {
                WeatherContent(
                    city = city,
                    weather = weatherState.weather,
                    onSearchClick = onNavigateToCitySearch
                )
            }
        }
    }
}

@Composable
private fun WeatherContent(
    weather: Weather,
    onSearchClick: () -> Unit,
    city: City
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WeatherDarkBackground)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .statusBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            item {
                WeatherTopBar(
                    cityName = city.name,
                    date = weather.current.date.formatDate(),
                    onSearchClick = onSearchClick
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                MainWeatherDisplay(weather = weather.current)
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                WeatherStatsRow(
                    weather = weather.current,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                HourlySection(weather = weather)
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                WeeklySection(weather = weather)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

private fun Instant.formatDate(): String {
    val formatter = DateTimeFormatter.ofPattern("EEEE, d MMM", Locale.ENGLISH)
    return this
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}

@Composable
private fun WeatherTopBar(
    cityName: String,
    date: String,
    onSearchClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = cityName,
                color = WeatherTextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = date,
                color = WeatherTextSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        }
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(WeatherCardBackground)
                .clickable { onSearchClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search City",
                tint = WeatherTextPrimary,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun MainWeatherDisplay(weather: CurrentWeather) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = weather.condition.weatherIcon(),
            contentDescription = weather.condition.label,
            tint = weather.condition.weatherIconTint(),
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "${weather.temperatureCelsius}°C",
            color = WeatherTextPrimary,
            fontSize = 72.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 76.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = weather.condition.label,
            color = WeatherTextSecondary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview
@Composable
private fun WeatherContentPreview() {
    WeatherTheme {
        WeatherContent(
            weather = SampleWeather,
            onSearchClick = {},
            city = City.Default
        )
    }
}

private val SampleWeather = Weather(
    current = CurrentWeather(
        cityName = "Taipei",
        date = Instant.now(),
        temperatureCelsius = 25,
        condition = WeatherCondition.SUNNY,
        feelsLikeCelsius = 27,
        humidityPercent = 60,
        windSpeedKmh = 12
    ),
    hourlyForecasts = List(24) { i ->
        HourlyForecast(
            time = LocalTime.of(i, 0),
            temperatureCelsius = 20 + (i % 10),
            condition = if (i % 3 == 0) WeatherCondition.CLOUDY else WeatherCondition.SUNNY
        )
    },
    weeklyForecasts = List(7) { i ->
        DailyForecast(
            date = LocalDate.now().plusDays(i.toLong()),
            highCelsius = 28,
            lowCelsius = 22,
            condition = if (i % 2 == 0) WeatherCondition.SUNNY else WeatherCondition.RAINY
        )
    }
)
