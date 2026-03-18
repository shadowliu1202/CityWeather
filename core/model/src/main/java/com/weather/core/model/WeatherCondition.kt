package com.weather.core.model

enum class WeatherCondition(val label: String, val iconRes: String) {
    SUNNY("Sunny", "sunny"),
    CLOUDY("Cloudy", "cloudy"),
    PARTLY_CLOUDY("Partly Cloudy", "partly_cloudy"),
    RAINY("Rainy", "rainy"),
    STORMY("Stormy", "stormy"),
    SNOWY("Snowy", "snowy"),
    WINDY("Windy", "windy"),
    FOGGY("Foggy", "foggy")
}
