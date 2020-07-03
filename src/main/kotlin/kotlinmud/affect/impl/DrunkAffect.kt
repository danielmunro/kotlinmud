package kotlinmud.affect.impl

import kotlinmud.affect.type.Affect
import kotlinmud.affect.model.AffectInstance
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.model.AttributesBuilder
import kotlinmud.helper.Noun
import kotlinmud.io.model.Message
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.model.Mob

class DrunkAffect : Affect {
    override val type: AffectType = AffectType.DRUNK

    override fun messageFromInstantiation(mob: Mob, target: Noun?): Message {
        return MessageBuilder()
            .toActionCreator("Your pulse speeds up as you are consumed by rage!")
            .toObservers("$mob's pulse speeds up as they are consumed by rage!")
            .build()
    }

    override fun messageFromWearOff(target: Noun): Message {
        return MessageBuilder()
            .toActionCreator("Your heart rate returns to normal.")
            .toObservers("$target's heart rate returns to normal.")
            .build()
    }

    override fun createInstance(timeout: Int): AffectInstance {
        return AffectInstance(
            type,
            timeout,
            AttributesBuilder()
                .dexterity(-1)
                .intelligence(-1)
                .build()
        )
    }
}
