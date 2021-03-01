package kotlinmud

import kotlinmud.app.createContainer
import kotlinmud.db.applySchema
import kotlinmud.db.createConnection
import kotlinmud.item.service.ItemService
import kotlinmud.mob.service.MobService
import kotlinmud.world.createWorld
import org.kodein.di.erased.instance

const val width = 100
const val length = 100

fun main() {
    createConnection()
    applySchema()
    val container = createContainer(9999)
    val mobService by container.instance<MobService>()
    val itemService by container.instance<ItemService>()
    createWorld(mobService, itemService)
}
