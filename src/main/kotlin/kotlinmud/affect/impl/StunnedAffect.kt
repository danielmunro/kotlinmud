package kotlinmud.affect.impl

import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.model.Affect
import kotlinmud.affect.type.AffectInterface
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.helper.Noun
import kotlinmud.io.model.Message
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.model.Mob
import org.jetbrains.exposed.sql.transactions.transaction

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
        return createAffect(type, timeout, transaction { AttributesDAO.new { intelligence = -1 } })
    }
}
