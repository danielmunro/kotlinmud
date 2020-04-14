package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.observer.Observer
import kotlinmud.service.WeatherService

class ChangeWeatherObserver(private val weatherService: WeatherService) : Observer {
    override val eventType: EventType = EventType.TICK

    override fun <T> processEvent(event: Event<T>) {
        weatherService.changeWeather()
    }
}
