package kotlinmud.affect.impl

import kotlinmud.affect.model.AffectInstance
import kotlinmud.affect.type.Affect
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.model.AttributesBuilder
import kotlinmud.helper.Noun
import kotlinmud.io.model.Message
import kotlinmud.mob.dao.MobDAO

class StunnedAffect : Affect {
    override val type: AffectType = AffectType.STUNNED

    override fun messageFromInstantiation(mob: MobDAO, target: Noun?): Message {
        TODO("Not yet implemented")
    }

    override fun messageFromWearOff(target: Noun): Message {
        TODO("Not yet implemented")
    }

    override fun createInstance(timeout: Int): AffectInstance {
        return AffectInstance(
            type, timeout, AttributesBuilder()
                .intelligence(-1)
                .build()
        )
    }
}
