package kotlinmud.service

import kotlinmud.weather.Weather

class WeatherService {
    private var weather: Weather = Weather.CLEAR

    fun changeWeather() {
        val plusOne = Weather.values().indexOf(weather) + 1
        val next = if (plusOne == Weather.values().size) 0 else plusOne
        weather = Weather.values()[next]
    }
}
