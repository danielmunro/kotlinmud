package kotlinmud.affect.impl

import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.model.Affect
import kotlinmud.affect.type.AffectInterface
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.type.Attribute
import kotlinmud.helper.Noun
import kotlinmud.io.model.Message
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.model.Mob
import org.jetbrains.exposed.sql.transactions.transaction

class DrunkAffect : AffectInterface {
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

    override fun createInstance(timeout: Int): Affect {
        return Affect(
            type,
            timeout,
            mapOf(
                Pair(Attribute.DEX, -1),
                Pair(Attribute.INT, -1),
            )
        )
    }
}
