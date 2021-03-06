package kotlinmud.affect.impl

import kotlinmud.affect.model.Affect
import kotlinmud.affect.type.AffectInterface
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.type.Attribute
import kotlinmud.helper.Noun
import kotlinmud.io.model.Message
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.model.Mob

class StunnedAffect : AffectInterface {
    override val type: AffectType = AffectType.STUNNED

    override fun messageFromInstantiation(mob: Mob, target: Noun?): Message {
        return MessageBuilder()
            .toActionCreator("$target is temporarily stunned.")
            .toTarget("you are stunned.")
            .build()
    }

    override fun messageFromWearOff(target: Noun): Message {
        return MessageBuilder()
            .toTarget("you regain your senses.")
            .build()
    }

    override fun createInstance(timeout: Int): Affect {
        return Affect(
            type,
            timeout,
            mapOf(
                Pair(Attribute.INT, -1),
                Pair(Attribute.DEX, -1),
            )
        )
    }
}
