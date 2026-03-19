package com.weather.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.weather.feature.city.presentation.CityScreen
import com.weather.feature.city.presentation.cityScreen
import com.weather.feature.weather.presentation.WeatherScreen
import com.weather.feature.weather.presentation.weatherScreen

@Composable
fun WeatherNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = WeatherScreen::class
    ) {
        weatherScreen(
            onNavigateToCitySearch = {
                navController.navigate(CityScreen)
            }
        )
        cityScreen(
            onNavigateBack = navController::popBackStack,
            onCitySelected = { city ->
                navController.navigate(WeatherScreen(city)) {
                    popUpTo(WeatherScreen::class) {
                        inclusive = true
                    }
                }
            }
        )
    }
}
