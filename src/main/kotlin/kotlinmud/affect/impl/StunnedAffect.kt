package kotlinmud.affect.impl

import kotlinmud.affect.dao.AffectDAO
import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.type.Affect
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.dao.AttributesDAO
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

    override fun createInstance(timeout: Int): AffectDAO {
        return createAffect(type, timeout, AttributesDAO.new { intelligence = -1 })
    }
}
