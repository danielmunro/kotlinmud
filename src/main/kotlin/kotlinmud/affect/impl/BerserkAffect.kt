package kotlinmud.affect.impl

import kotlinmud.affect.model.AffectInstance
import kotlinmud.affect.type.Affect
import kotlinmud.affect.type.AffectType
import kotlinmud.helper.Noun
import kotlinmud.io.model.Message
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.dao.MobDAO

class BerserkAffect : Affect {
    override val type: AffectType = AffectType.BERSERK

    override fun messageFromInstantiation(mob: MobDAO, target: Noun?): Message {
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
        return AffectInstance(type, timeout)
    }
}
