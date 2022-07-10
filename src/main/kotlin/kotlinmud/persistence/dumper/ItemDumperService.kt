package kotlinmud.persistence.dumper

import kotlinmud.persistence.model.ItemModel
import kotlinmud.persistence.parser.TokenType
import kotlinmud.persistence.spec.ItemSpec
import kotlinmud.room.model.Area

class ItemDumperService(area: Area, private val items: List<ItemModel>) {
    private val itemSpec = ItemSpec(area)

    fun dump(): String {
        var buffer = if (items.isNotEmpty()) "items:\n" else ""
        items.forEach { item ->
            itemSpec.tokens.forEach { token ->
                buffer += when (token.token) {
                    TokenType.ID -> "${item.id}. "
                    TokenType.Name -> item.name + "\n"
                    TokenType.Brief -> item.brief + "\n"
                    TokenType.Description -> item.description + "~\n"
                    TokenType.Props -> dumpProps(item)
                    TokenType.ItemRespawn -> dumpRespawns(item)
                    else -> ""
                }
            }
            buffer += "\n"
        }
        return buffer
    }

    private fun dumpRespawns(item: ItemModel): String {
        return "${item.respawns.joinToString("\n") { "${it.type.value} ${it.maxAmount} ${it.maxInGame} ${it.targetId}~" }}\n~\n"
    }

    private fun dumpProps(item: ItemModel): String {
        return "${item.keywords.map { entry -> "${entry.key} ${entry.value}~" }.joinToString("\n")}\n~\n"
    }
}
