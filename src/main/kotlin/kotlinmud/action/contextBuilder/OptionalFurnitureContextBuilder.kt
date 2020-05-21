package kotlinmud.action.contextBuilder

import kotlinmud.action.Context
import kotlinmud.action.Status
import kotlinmud.io.Syntax
import kotlinmud.item.Item

class OptionalFurnitureContextBuilder(private val itemsInRoom: List<Item>) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return itemsInRoom.find {
            kotlinmud.string.matches(it.name, word)
        }?.let {
            Context(syntax, Status.OK, it)
        } ?: Context(syntax, Status.FAILED, "you can't find that anywhere.")
    }
}