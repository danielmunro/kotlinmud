package kotlinmud.action.impl.info

import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.model.createResponseWithEmptyActionContext
import kotlinmud.weather.type.Weather

fun createWeatherAction(): Action {
    return Action(Command.WEATHER) {
        if (it.getRoom().isIndoors)
            createResponseWithEmptyActionContext(messageToActionCreator("You can't see the weather indoors."))
        else createResponseWithEmptyActionContext(
            messageToActionCreator(
                when (it.getWeather()) {
                    Weather.BLIZZARD -> "A blizzard blows fiercely."
                    Weather.BLUSTERY -> "A strong wind blows from the west."
                    Weather.CLEAR -> "The sun beams on a cloudless day."
                    Weather.OVERCAST -> "A low layer of clouds obscure the sun."
                    Weather.STORMING -> "Thunder and lightning come crashing down from above."
                }
            )
        )
    }
}
