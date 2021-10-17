package kotlinmud.world.impl.itrias.troy.city

import kotlinmud.affect.model.Affect
import kotlinmud.affect.type.AffectType
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.item.type.Weapon
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.impl.Elf
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.race.impl.Lasher
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.room.model.Room
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction
import kotlinmud.room.type.RegenLevel
import kotlinmud.world.service.AreaBuilderService

fun createTroyEastSide(areaBuilderService: AreaBuilderService, connection: Room) {
    areaBuilderService.startWith(connection)
        .buildRoom("sunrise1", Direction.EAST) {
            it.name = "Sunrise Avenue"
        }
        .buildRoom("sunrise2", Direction.EAST)
        .buildRoom("sunrise3", Direction.EAST)
        .connectTo("east gate", Direction.EAST)
        .startWith("sunrise1")
        .buildRoom("regen room", Direction.NORTH) {
            it.name = "A quiet place to sit and meditate"
            it.regenLevel = RegenLevel.HIGH
            it.items = mutableListOf(
                areaBuilderService.itemBuilder(
                    "a donation pit",
                    "tbd",
                ).also { item ->
                    item.type = ItemType.CONTAINER
                    item.canOwn = false
                    item.material = Material.STONE
                    item.items = listOf()
                }.build()
            )
        }
        .startWith("sunrise1")
        .buildRoom("training room", Direction.SOUTH) {
            it.name = "Endurance and training judo"
        }
        .startWith("sunrise2")
        .buildRoom("warehouse", Direction.SOUTH) {
            it.name = "An abandoned warehouse"
        }
        .startWith("sunrise3")
        .buildRoom("general store", Direction.NORTH) {
            it.name = "General store & crafting supplies"
        }
        .startWith("sunrise3")
        .buildRoom("tavern", Direction.SOUTH) {
            it.name = "A shady tavern"
        }

    areaBuilderService.startWith("training room")
        .buildTrainer(
            "Ailred",
            "Ailred the master trainer is here, meditating over her practice",
            "tbd",
            Elf(),
        )

    areaBuilderService.startWith("regen room")
        .buildHealer(
            "Zannor the healer",
            "the healer is here, ready to dispense some spells",
            "tbd",
            Human(),
            listOf(
                Triple(SkillType.CURE_LIGHT, 90, 800),
                Triple(SkillType.CURE_SERIOUS, 80, 1800),
                Triple(SkillType.HEAL, 70, 8000),
                Triple(SkillType.ARMOR, 75, 400),
                Triple(SkillType.BLESS, 75, 600),
                Triple(SkillType.CURE_POISON, 80, 400),
                Triple(SkillType.REMOVE_CURSE, 80, 400),
                Triple(SkillType.CURE_BLINDNESS, 80, 400),
                Triple(SkillType.FEAST, 80, 800),
            ),
        )

    createTemple(
        areaBuilderService.copy(Area.TempleMatook),
        areaBuilderService.getRoomFromLabel("sunrise2"),
    )

    areaBuilderService.startWith("tavern")
        .buildShopkeeper(
            "Nikleath Tash the barkeep",
            "a hard-looking barkeep is here",
            "tbd",
            Lasher(),
            mapOf(),
        ) {
            it.messages = listOf(
                "what d'you want?",
                "bar's closed",
            )
        }

    areaBuilderService.startWith("general store")
        .buildShopkeeper(
            "Wyon the merchant",
            "a merchant is here, organizing his wares",
            "tbd",
            Human(),
            mapOf(
                Pair(
                    areaBuilderService.itemBuilder(
                        "scraps of leather",
                        "tbd",
                        0.3,
                        20,
                    ).also {
                        it.material = Material.LEATHER
                        it.type = ItemType.CRAFTABLE
                    },
                    10,
                ),
                Pair(
                    areaBuilderService.itemBuilder(
                        "iron ingot",
                        "tbd",
                        0.3,
                        20,
                    ).also {
                        it.material = Material.IRON
                        it.type = ItemType.CRAFTABLE
                    },
                    3,
                ),
                Pair(
                    areaBuilderService.itemBuilder(
                        "planks of lumber",
                        "tbd",
                        10.0,
                        40,
                    ).also {
                        it.material = Material.WOOD
                        it.type = ItemType.CRAFTABLE
                    },
                    5,
                ),
                Pair(
                    areaBuilderService.itemBuilder(
                        "a torch",
                        "tbd",
                        0.3,
                        10,
                    ).also {
                        it.type = ItemType.EQUIPMENT
                        it.material = Material.WOOD
                        it.position = Position.HELD
                        it.affects = listOf(
                            Affect(AffectType.GLOWING),
                        )
                    },
                    10
                ),
                Pair(
                    areaBuilderService.itemBuilder(
                        "a pair of shears",
                        "tbd",
                        2.0,
                        100,
                    ).also {
                        it.type = ItemType.EQUIPMENT
                        it.weaponType = Weapon.SHEARS
                        it.damageType = DamageType.PIERCE
                        it.attackVerb = "slice"
                        it.material = Material.WOOD
                    },
                    3,
                ),
            ),
        )
}
