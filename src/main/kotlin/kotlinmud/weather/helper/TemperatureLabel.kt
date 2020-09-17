package kotlinmud.weather.helper

import kotlinmud.weather.type.Temperature

fun getTemperatureLabel(temperature: Temperature): String {
    return when (temperature) {
        Temperature.VERY_COLD -> "frozen"
        Temperature.COLD -> "cold"
        Temperature.TEMPERATE -> "mild"
        Temperature.WARM -> "warm"
        Temperature.HOT -> "hot"
        Temperature.VERY_HOT -> "scorching"
    }
}
