package kotlinmud.event.observer.impl.tick

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.weather.service.WeatherService

class ChangeWeatherObserver(private val weatherService: WeatherService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        weatherService.changeWeather()
    }
}
