package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.helper.string.matches
import kotlinmud.io.type.Syntax
import kotlinmud.item.model.Item

class OptionalFurnitureContextBuilder(private val itemsInRoom: List<Item>) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return itemsInRoom.find {
            word.matches(it.name)
        }?.let {
            Context<Any>(syntax, Status.OK, it)
        } ?: Context<Any>(
            syntax,
            Status.FAILED,
            "you can't find that anywhere."
        )
    }
}
