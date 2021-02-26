package kotlinmud.affect.impl

import kotlinmud.affect.dao.AffectDAO
import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.type.Affect
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.helper.Noun
import kotlinmud.io.model.Message
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.model.Mob
import org.jetbrains.exposed.sql.transactions.transaction

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

    override fun createInstance(timeout: Int): AffectDAO {
        return createAffect(type, timeout, transaction { AttributesDAO.new { dexterity = -1 } })
    }
}
