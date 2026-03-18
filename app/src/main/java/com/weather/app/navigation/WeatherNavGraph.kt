package com.weather.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.weather.feature.city.CityScreen
import com.weather.feature.weather.WeatherScreen

sealed class Screen(val route: String) {
    data object Weather : Screen("weather/{cityId}") {
        fun createRoute(cityId: String = "taipei") = "weather/$cityId"
    }
    data object City : Screen("city")
}

@Composable
fun WeatherNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Weather.createRoute("taipei")
    ) {
        composable(
            route = Screen.Weather.route,
            arguments = listOf(
                navArgument("cityId") {
                    type = NavType.StringType
                    defaultValue = "taipei"
                }
            )
        ) { backStackEntry ->
            val cityId = backStackEntry.arguments?.getString("cityId") ?: "taipei"
            WeatherScreen(
                cityId = cityId,
                onNavigateToCitySearch = {
                    navController.navigate(Screen.City.route)
                }
            )
        }

        composable(route = Screen.City.route) {
            CityScreen(
                onCitySelected = { cityId ->
                    navController.navigate(Screen.Weather.createRoute(cityId)) {
                        popUpTo(Screen.City.route) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
