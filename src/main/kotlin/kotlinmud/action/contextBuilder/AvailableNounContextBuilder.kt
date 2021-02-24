package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.affect.type.AffectType
import kotlinmud.helper.string.matches
import kotlinmud.io.type.Syntax
import kotlinmud.item.service.ItemService
import kotlinmud.mob.model.Mob
import kotlinmud.mob.repository.findMobsForRoom
import kotlinmud.room.dao.RoomDAO
import org.jetbrains.exposed.sql.transactions.transaction

class AvailableNounContextBuilder(
    private val itemService: ItemService,
    private val mob: Mob,
    private val room: RoomDAO
) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val target = transaction {
            findMobsForRoom(room).find {
                word.matches(it.name) && it.affects.find { affect -> affect.type == AffectType.INVISIBILITY } == null
            } ?: itemService.findByOwner(mob, word)
                ?: itemService.findByRoom(room, word)
        } ?: return Context(
            syntax,
            Status.FAILED,
            "you don't see anything like that here."
        )
        return Context(syntax, Status.OK, target)
    }
}
