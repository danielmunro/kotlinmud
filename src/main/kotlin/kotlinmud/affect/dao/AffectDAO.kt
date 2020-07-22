package kotlinmud.affect.dao

import kotlinmud.affect.table.Affects
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.item.dao.ItemDAO
import kotlinmud.mob.dao.MobDAO
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class AffectDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AffectDAO>(Affects)

    var type by Affects.type.transform(
        { it.toString() },
        { AffectType.valueOf(it) }
    )
    var timeout by Affects.timeout
    var mob by MobDAO optionalReferencedOn Affects.mobId
    var item by ItemDAO optionalReferencedOn Affects.itemId
    var attributes by AttributesDAO optionalReferencedOn Affects.attributesId
}
