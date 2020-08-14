package kotlinmud.room.helper

import kotlinmud.biome.type.BiomeType
import kotlinmud.room.dao.RoomDAO
import kotlinmud.weather.Temperature
import kotlinmud.weather.Weather
import kotlinmud.weather.helper.getTemperatureLabel

fun getRoomName(temperature: Temperature, biomeType: BiomeType): String {
    return "A ${getTemperatureLabel(temperature)} ${getBiomeLabel(biomeType)}"
}

fun getRoomDescription(room: RoomDAO, weather: Weather): String {
    val biomeSlug = when (room.biome) {
        BiomeType.ARBOREAL -> "A forest of evergreen trees."
        BiomeType.DESERT -> "Rolling dunes of sand spread out before you."
        BiomeType.JUNGLE -> "A dense jungle."
        BiomeType.BADLANDS -> "A sulphurous smell permeates rocky and unwelcoming terrain."
        BiomeType.MOUNTAIN -> "Mountainous rocks."
        BiomeType.PLAINS -> "A flat grassland."
        BiomeType.TUNDRA -> "A cold, flat land."
        BiomeType.UNDERGROUND -> "Surrounded by rock, deep underground."
        BiomeType.SKY -> "Flying in the sky!"
        BiomeType.NONE -> "Floating in nether."
    }

    val nearbySlug = room.getAllExits().entries.joinToString {
        "${getDirectionString(it.key)} ${getBiomeDescription(it.value)}."
    }

    val weatherSlug = when (weather) {
        Weather.BLIZZARD -> "A strong blizzard is bearing down."
        Weather.CLEAR -> "The sky is blue with barely a cloud in sight."
        Weather.BLUSTERY -> "Mighty gusts of wind blast against you."
        Weather.OVERCAST -> "A blanket of clouds gives a cover of gray skies."
        Weather.STORMING -> "Angry storm clouds boom thunder and lightning from the heavens."
    }

    return "$biomeSlug\n$nearbySlug\n$weatherSlug"
}
