package kotlinmud.mob.builder

import kotlinmud.affect.model.Affect
import kotlinmud.attributes.constant.startingHp
import kotlinmud.attributes.constant.startingMana
import kotlinmud.attributes.constant.startingMv
import kotlinmud.attributes.type.Attribute
import kotlinmud.item.model.Item
import kotlinmud.mob.model.Mob
import kotlinmud.mob.model.MobArguments
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.service.MobService
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.specialization.type.Specialization
import kotlinmud.mob.type.CurrencyType
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.Gender
import kotlinmud.mob.type.JobType
import kotlinmud.mob.type.MobCanonicalId
import kotlinmud.mob.type.Rarity
import kotlinmud.room.model.Room

open class MobBuilder(private val mobService: MobService) {
    var name = ""
    var brief = ""
    var description = ""
    var attributes = mutableMapOf(
        Pair(Attribute.HP, startingHp),
        Pair(Attribute.MANA, startingMana),
        Pair(Attribute.MV, startingMv),
    )
    var job = JobType.NONE
    var specialization: Specialization? = null
    var canonicalId: MobCanonicalId? = null
    var level = 1
    var hp = startingHp
    var mana = startingMana
    var mv = startingMana
    var gender = Gender.ANY
    var disposition = Disposition.STANDING
    var wimpy = 0
    var savingThrows = 0
    var rarity = Rarity.COMMON
    var equipped = listOf<Item>()
    var items = listOf<Item>()
    var maxItems = 0
    var maxWeight = 0
    var route = listOf<Room>()
    var skills = mutableMapOf<SkillType, Int>()
    var affects = mutableListOf<Affect>()
    var currencies = mutableMapOf<CurrencyType, Int>()
    lateinit var race: Race
    lateinit var room: Room

    open fun build(): Mob {
        return Mob(createMobArguments()).also { mobService.addMob(it) }
    }

    protected fun createMobArguments(): MobArguments {
        return MobArguments(
            name,
            brief,
            description,
            hp,
            mana,
            mv,
            level,
            race,
            disposition,
            job,
            specialization,
            gender,
            wimpy,
            savingThrows,
            rarity,
            canonicalId,
            attributes,
            room,
            equipped.toMutableList(),
            maxItems,
            maxWeight,
            items.toMutableList(),
            skills.toMutableMap(),
            affects.toMutableList(),
            currencies.toMutableMap(),
            route,
        )
    }
}
