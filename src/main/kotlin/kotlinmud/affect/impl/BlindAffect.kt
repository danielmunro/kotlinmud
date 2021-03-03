package kotlinmud.affect.impl

import kotlinmud.affect.model.Affect
import kotlinmud.affect.type.AffectInterface
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.type.Attribute
import kotlinmud.helper.Noun
import kotlinmud.io.model.Message
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.model.Mob

class BlindAffect : AffectInterface {
    override val type: AffectType = AffectType.BLIND

    override fun messageFromInstantiation(mob: Mob, target: Noun?): Message {
        return MessageBuilder()
            .toActionCreator("You are blinded.")
            .toObservers("$mob is blinded.")
            .build()
    }

    override fun messageFromWearOff(target: Noun): Message {
        return MessageBuilder()
            .toActionCreator("You can see again.")
            .toObservers("$target can see again.")
            .build()
    }

    override fun createInstance(timeout: Int): Affect {
        return Affect(
            type,
            timeout,
            mapOf(Pair(Attribute.DEX, -1))
        )
    }
}
