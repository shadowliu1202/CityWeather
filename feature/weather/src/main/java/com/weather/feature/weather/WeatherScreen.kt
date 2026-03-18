package com.weather.feature.weather

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weather.core.model.Weather
import com.weather.core.ui.theme.WeatherAccentBlue
import com.weather.core.ui.theme.WeatherCardBackground
import com.weather.core.ui.theme.WeatherDarkBackground
import com.weather.core.ui.theme.WeatherSunYellow
import com.weather.core.ui.theme.WeatherTextPrimary
import com.weather.core.ui.theme.WeatherTextSecondary
import com.weather.feature.weather.components.HourlyForecastRow
import com.weather.feature.weather.components.WeatherStatsRow
import com.weather.feature.weather.components.WeeklyForecastSection
import com.weather.feature.weather.util.weatherIcon
import com.weather.feature.weather.util.weatherIconTint

@Composable
fun WeatherScreen(
    cityId: String,
    onNavigateToCitySearch: () -> Unit,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    LaunchedEffect(cityId) {
        viewModel.loadWeather(cityId)
    }

    val weatherState by viewModel.weatherState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WeatherDarkBackground)
    ) {
        when (val state = weatherState) {
            is WeatherUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = WeatherAccentBlue
                )
            }

            is WeatherUiState.Error -> {
                Text(
                    text = "Error: ${state.message}",
                    color = WeatherTextPrimary,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }

            is WeatherUiState.Success -> {
                WeatherContent(
                    weather = state.weather,
                    onSearchClick = onNavigateToCitySearch
                )
            }
        }
    }
}

@Composable
private fun WeatherContent(
    weather: Weather,
    onSearchClick: () -> Unit
) {
    var selectedNavItem by remember { mutableStateOf(0) }

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
                // Top App Bar
                WeatherTopBar(
                    cityName = weather.cityName,
                    date = weather.date,
                    onSearchClick = onSearchClick
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                // Main Weather Display
                MainWeatherDisplay(weather = weather)
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                // Stats Row
                WeatherStatsRow(
                    weather = weather,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                // Today / Hourly Section
                HourlySection(weather = weather)
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                // Weekly Forecast Section
                WeeklySection(weather = weather)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Bottom Navigation Bar
        BottomNavigationBar(
            selectedItem = selectedNavItem,
            onItemSelected = { selectedNavItem = it }
        )
    }
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
private fun MainWeatherDisplay(weather: Weather) {
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

@Composable
private fun HourlySection(weather: Weather) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Today",
                color = WeatherTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Hourly",
                color = WeatherAccentBlue,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        HourlyForecastRow(
            hourlyForecasts = weather.hourlyForecasts,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
    }
}

@Composable
private fun WeeklySection(weather: Weather) {
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

@Composable
private fun BottomNavigationBar(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit
) {
    val navItems = listOf(
        Pair(Icons.Default.Home, "Home"),
        Pair(Icons.Default.Map, "Map"),
        Pair(Icons.Default.List, "List"),
        Pair(Icons.Default.Settings, "Settings")
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(WeatherCardBackground)
            .navigationBarsPadding()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        navItems.forEachIndexed { index, (icon, label) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onItemSelected(index) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (selectedItem == index) WeatherAccentBlue else WeatherTextSecondary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = label,
                    color = if (selectedItem == index) WeatherAccentBlue else WeatherTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = if (selectedItem == index) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}
