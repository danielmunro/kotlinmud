package kotlinmud.attributes.factory

import kotlinmud.attributes.dao.AttributesDAO
import org.jetbrains.exposed.sql.transactions.transaction

fun createStats(str: Int, int: Int, wis: Int, dex: Int, con: Int, hit: Int = 0, dam: Int = 0): AttributesDAO {
    return transaction {
        AttributesDAO.new {
            strength = str
            intelligence = int
            wisdom = wis
            dexterity = dex
            constitution = con
            this.hit = hit
            this.dam = dam
        }
    }
}

fun emptyAttributes(): AttributesDAO {
    return transaction { AttributesDAO.new {} }
}
