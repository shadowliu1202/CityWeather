package com.weather.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.weather.core.model.City
import com.weather.feature.city.presentation.CityRoute
import com.weather.feature.city.presentation.cityScreen
import com.weather.feature.weather.presentation.WeatherRoute
import com.weather.feature.weather.presentation.weatherScreen

@Composable
fun WeatherNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = WeatherRoute(City.Default)
    ) {
        weatherScreen(
            onNavigateToCitySearch = {
                navController.navigate(CityRoute)
            }
        )
        cityScreen(
            onNavigateBack = navController::popBackStack,
            onCitySelected = { city ->
                navController.navigate(WeatherRoute(city)) {
                    popUpTo(WeatherRoute::class) {
                        inclusive = true
                    }
                }
            }
        )
    }
}
