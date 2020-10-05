package kotlinmud.event.observer.impl.tick

import kotlinmud.weather.service.WeatherService

fun changeWeatherEvent(weatherService: WeatherService) {
    weatherService.changeWeather()
}
