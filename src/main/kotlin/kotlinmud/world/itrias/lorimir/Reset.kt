package kotlinmud.world.itrias.lorimir

import kotlinmud.item.helper.ItemBuilder
import kotlinmud.item.repository.countItemsByCanonicalId
import kotlinmud.item.type.Food
import kotlinmud.item.type.ItemCanonicalId
import kotlinmud.item.type.Material
import kotlinmud.mob.helper.MobBuilder
import kotlinmud.mob.race.impl.Canid
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.repository.findRoomsByArea
import kotlinmud.room.type.Area

fun resetForest() {
    val rooms = findRoomsByArea(Area.LorimirForest)

    val mushroomBuilder = ItemBuilder("a small brown mushroom")
        .description("foo")
        .material(Material.ORGANIC)
        .food(Food.MUSHROOM)
        .canonicalId(ItemCanonicalId.Mushroom)

    val count = countItemsByCanonicalId(ItemCanonicalId.Mushroom)
    var amountToRespawn = Math.min(10 - count, 10)

    val randomSubset = rooms.filter { Math.random() < 0.3 }
    var i = 0

    while (amountToRespawn > 0 && i < randomSubset.size) {
        mushroomBuilder.room(randomSubset[i]).build()
        amountToRespawn -= 1
        i += 1
    }

//    val foxBuilder = MobBuilder()
//        .name("a small fox")
//        .brief("a small fox darts through the underbrush")
//        .description("a small fox is here.")
//        .level(3)
//        .race(Canid())
//
//    rooms.filter { Math.random() < 0.1 }.forEach {
//        foxBuilder.room(it).build()
//    }
}
