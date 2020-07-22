package kotlinmud.affect.factory

import kotlinmud.affect.dao.AffectDAO
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.dao.AttributesDAO
import org.jetbrains.exposed.sql.transactions.transaction

fun createAffect(affectType: AffectType, timeout: Int? = null, attributes: AttributesDAO? = null): AffectDAO {
    return transaction {
        AffectDAO.new {
            type = affectType
            this.timeout = timeout
            this.attributes = attributes ?: AttributesDAO.new {}
        }
    }
}

fun affects(affectType: AffectType): MutableList<AffectDAO> {
    return mutableListOf(createAffect(affectType))
}
