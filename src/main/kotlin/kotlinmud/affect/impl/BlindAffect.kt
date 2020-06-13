package kotlinmud.affect.impl

import kotlinmud.affect.Affect
import kotlinmud.affect.model.AffectInstance
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.model.AttributesBuilder
import kotlinmud.helper.Noun
import kotlinmud.io.model.Message
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.model.Mob

class BlindAffect : Affect {
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

    override fun createInstance(timeout: Int): AffectInstance {
        return AffectInstance(
            type, timeout, AttributesBuilder()
                .dexterity(-1)
                .build()
        )
    }
}
