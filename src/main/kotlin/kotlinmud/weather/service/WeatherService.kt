package kotlinmud.weather.service

import kotlinmud.weather.type.Temperature
import kotlinmud.weather.type.Weather

class WeatherService {
    private var weather: Weather = Weather.CLEAR

    fun changeWeather() {
        val plusOne = Weather.values().indexOf(weather) + 1
        val next = if (plusOne == Weather.values().size) 0 else plusOne
        weather = Weather.values()[next]
    }

    fun getWeather(): Weather {
        return weather
    }

    fun getTemperature(): Temperature {
        return when (weather) {
            Weather.STORMING -> Temperature.COLD
            Weather.OVERCAST -> Temperature.TEMPERATE
            Weather.BLIZZARD -> Temperature.VERY_COLD
            Weather.BLUSTERY -> Temperature.COLD
            Weather.CLEAR -> Temperature.WARM
        }
    }
}
