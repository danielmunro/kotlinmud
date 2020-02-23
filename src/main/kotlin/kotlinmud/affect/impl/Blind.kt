package kotlinmud.affect.impl

import kotlinmud.affect.Affect
import kotlinmud.affect.AffectType
import kotlinmud.io.Message
import kotlinmud.mob.Mob

class Blind : Affect {
    override val type: AffectType = AffectType.BLIND

    override fun messageFromInstantiation(mob: Mob, target: Mob?): Message {
        return Message("You are blinded.", "$mob is blinded.")
    }

    override fun messageFromWearOff(mob: Mob): Message {
        return Message("You can see again.", "$mob can see again.")
    }
}
