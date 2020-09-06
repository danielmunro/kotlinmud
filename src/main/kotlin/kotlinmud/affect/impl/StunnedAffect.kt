package kotlinmud.affect.impl

import kotlinmud.affect.dao.AffectDAO
import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.type.Affect
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.helper.Noun
import kotlinmud.io.model.Message
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.dao.MobDAO

class StunnedAffect : Affect {
    override val type: AffectType = AffectType.STUNNED

    override fun messageFromInstantiation(mob: MobDAO, target: Noun?): Message {
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

    override fun createInstance(timeout: Int): AffectDAO {
        return createAffect(type, timeout, AttributesDAO.new { intelligence = -1 })
    }
}
