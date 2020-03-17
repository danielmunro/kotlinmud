package kotlinmud.affect.impl

import kotlinmud.affect.Affect
import kotlinmud.affect.AffectInstance
import kotlinmud.affect.AffectType
import kotlinmud.io.Message
import kotlinmud.mob.Mob

class BerserkAffect : Affect {
    override val type: AffectType = AffectType.BERSERK

    override fun messageFromInstantiation(mob: Mob, target: Mob?): Message {
        return Message(
            "Your pulse speeds up as you are consumed by rage!",
            "$mob's pulse speeds up as they are consumed by rage!")
    }

    override fun messageFromWearOff(mob: Mob): Message {
        return Message(
            "Your heart rate returns to normal.",
            "$mob's heart rate returns to normal.")
    }

    override fun createInstance(timeout: Int): AffectInstance {
        return AffectInstance(type, timeout)
    }
}
