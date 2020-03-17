package kotlinmud.affect.impl

import kotlinmud.affect.Affect
import kotlinmud.affect.AffectInstance
import kotlinmud.affect.AffectType
import kotlinmud.attributes.Attributes
import kotlinmud.io.Message
import kotlinmud.mob.Mob

class BlindAffect : Affect {
    override val type: AffectType = AffectType.BLIND

    override fun messageFromInstantiation(mob: Mob, target: Mob?): Message {
        return Message("You are blinded.", "$mob is blinded.")
    }

    override fun messageFromWearOff(mob: Mob): Message {
        return Message("You can see again.", "$mob can see again.")
    }

    override fun createInstance(timeout: Int): AffectInstance {
        return AffectInstance(type, timeout, Attributes.Builder()
            .setDex(-1)
            .build())
    }
}
