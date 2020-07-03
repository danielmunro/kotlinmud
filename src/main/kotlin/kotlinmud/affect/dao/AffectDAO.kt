package kotlinmud.affect.dao

import kotlinmud.affect.table.Affects
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.table.Items
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.table.Mobs
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class AffectDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AffectDAO>(Affects)

    var affect by Affects.affect
    val mob by MobDAO referrersOn Mobs.id
    val item by ItemDAO referrersOn Items.id
}