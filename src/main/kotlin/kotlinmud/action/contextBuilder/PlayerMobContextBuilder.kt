package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.io.type.Syntax
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.table.Mobs
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class PlayerMobContextBuilder() : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return transaction {
            (
                Mobs.select {
                    Mobs.name eq word
                }.firstOrNull() ?: Mobs.select {
                    Mobs.name like "$word%"
                }.firstOrNull()
                )?.let {
                MobDAO.wrapRow(it)
            }
        }?.let {
            Context<Any>(syntax, Status.OK, it)
        } ?: Context<Any>(syntax, Status.ERROR, "they aren't here.")
    }
}
