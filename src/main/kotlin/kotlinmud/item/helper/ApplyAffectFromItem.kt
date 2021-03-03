package kotlinmud.item.helper

import kotlinmud.affect.model.Affect
import kotlinmud.item.model.Item
import kotlinmud.mob.model.Mob

fun applyAffectFromItem(mob: Mob, item: Item) {
    item.affects.forEach {
        mob.affects.add(Affect(it.type, it.timeout, it.attributes))
    }
}
