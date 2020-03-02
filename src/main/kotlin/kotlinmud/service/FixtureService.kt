package kotlinmud.service

import io.github.serpro69.kfaker.Faker
import kotlinmud.attributes.*
import kotlinmud.item.Inventory
import kotlinmud.item.Item
import kotlinmud.loader.Area
import kotlinmud.mob.Disposition
import kotlinmud.mob.Mob
import kotlinmud.mob.SpecializationType
import kotlinmud.mob.race.Race
import kotlinmud.mob.race.impl.Human
import kotlinmud.reset.MobReset

class FixtureService {
    private var mobs = 0
    private var items = 0
    private val faker = Faker()

    fun populateWorld(area: Area, mobService: MobService) {
        mobService.addMobReset(
            MobReset(
                area.mobs[0],
                mobService.getStartRoom(),
                1,
                1
            )
        )
        mobService.respawnWorld()
        createItem(mobService.getStartRoom().inventory)
    }

    fun createMob(race: Race = Human(), specialization: SpecializationType = SpecializationType.NONE): Mob {
        mobs++
        return Mob(
                mobs,
                faker.name.name(),
                "A test mob is here ($mobs).",
                Disposition.STANDING,
                startingHp,
                startingMana,
                startingMv,
                1,
                race,
                specialization,
                createDefaultMobAttributes(),
                Inventory(),
                Inventory(),
                mapOf(),
                mutableListOf()
        )
    }

    fun createItem(inv: Inventory, attributes: Attributes = Attributes()): Item {
        items++
        val item = Item(
            items,
            "the ${faker.cannabis.strains()} of ${faker.ancient.hero()}",
            "A test item is here ($items).",
            1.0,
            attributes)
        inv.items.add(item)
        return item
    }
}
