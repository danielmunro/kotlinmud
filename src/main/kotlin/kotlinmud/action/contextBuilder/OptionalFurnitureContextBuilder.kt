package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.io.Syntax
import kotlinmud.item.Item

class OptionalFurnitureContextBuilder(private val itemsInRoom: List<Item>) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return itemsInRoom.find {
            kotlinmud.string.matches(it.name, word)
        }?.let {
            Context<Any>(syntax, Status.OK, it)
        } ?: Context<Any>(
            syntax,
            Status.FAILED,
            "you can't find that anywhere."
        )
    }
}
