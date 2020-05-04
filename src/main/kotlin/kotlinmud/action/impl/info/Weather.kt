package kotlinmud.action.impl.info

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.Syntax
import kotlinmud.io.createResponseWithEmptyActionContext
import kotlinmud.weather.Weather

fun createWeatherAction(): Action {
    return Action(
        Command.WEATHER,
        mustBeAlert(),
        listOf(Syntax.COMMAND)) {
            if (it.getRoom().isIndoor)
                createResponseWithEmptyActionContext(Message("You can't see the weather indoors."))
            else createResponseWithEmptyActionContext(
                Message(
                    when (it.getWeather()) {
                        Weather.BLIZZARD -> "A blizzard blows fiercely."
                        Weather.BLUSTERY -> "A strong wind blows from the west."
                        Weather.CLEAR -> "The sun beams on a cloudless day."
                        Weather.OVERCAST -> "A low layer of clouds obscure the sun."
                        Weather.STORMING -> "Thunder and lightning come crashing down from above."
                    }
                ))
        }
}
