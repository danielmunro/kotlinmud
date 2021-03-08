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
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.room.model.Room

open class MobBuilder(private val mobService: MobService) {
    private var name = ""
    private var brief = ""
    private var description = ""
    private var attributes = mutableMapOf(
        Pair(Attribute.HP, startingHp),
        Pair(Attribute.MANA, startingMana),
        Pair(Attribute.MV, startingMv),
    )
    private var job = JobType.NONE
    private var specialization: Specialization? = null
    private var canonicalId: MobCanonicalId? = null
    private var level = 1
    private var hp = startingHp
    private var mana = startingMana
    private var mv = startingMana
    private var gender = Gender.ANY
    private var disposition = Disposition.STANDING
    private var wimpy = 0
    private var savingThrows = 0
    private var rarity = Rarity.COMMON
    private var equipped = listOf<Item>()
    private var items = listOf<Item>()
    private var maxItems = 0
    private var maxWeight = 0
    private var route = listOf<Room>()
    private var skills = mapOf<SkillType, Int>()
    private var affects = listOf<Affect>()
    private var currencies = mapOf<CurrencyType, Int>()
    private var card: MobCardDAO? = null
    private lateinit var race: Race
    private lateinit var room: Room

    fun name(value: String): MobBuilder {
        name = value
        return this
    }

    fun brief(value: String): MobBuilder {
        brief = value
        return this
    }

    fun description(value: String): MobBuilder {
        description = value
        return this
    }

    fun room(value: Room): MobBuilder {
        room = value
        return this
    }

    fun attributes(value: MutableMap<Attribute, Int>): MobBuilder {
        attributes = (attributes + value).toMutableMap()
        return this
    }

    fun job(value: JobType): MobBuilder {
        job = value
        return this
    }

    fun route(value: List<Room>): MobBuilder {
        route = value
        return this
    }

    fun race(value: Race): MobBuilder {
        race = value
        return this
    }

    fun canonicalId(value: MobCanonicalId): MobBuilder {
        canonicalId = value
        return this
    }

    fun level(value: Int): MobBuilder {
        level = value
        return this
    }

    fun gender(value: Gender): MobBuilder {
        gender = value
        return this
    }

    fun disposition(value: Disposition): MobBuilder {
        disposition = value
        return this
    }

    fun wimpy(value: Int): MobBuilder {
        wimpy = value
        return this
    }

    fun savingThrows(value: Int): MobBuilder {
        savingThrows = value
        return this
    }

    fun rarity(value: Rarity): MobBuilder {
        rarity = value
        return this
    }

    fun equipped(value: List<Item>): MobBuilder {
        equipped = value
        return this
    }

    fun maxItems(value: Int): MobBuilder {
        maxItems = value
        return this
    }

    fun maxWeight(value: Int): MobBuilder {
        maxWeight = value
        return this
    }

    fun items(value: List<Item>): MobBuilder {
        items = value
        return this
    }

    fun skills(value: Map<SkillType, Int>): MobBuilder {
        skills = value
        return this
    }

    fun affects(value: List<Affect>): MobBuilder {
        affects = value
        return this
    }

    fun currencies(value: Map<CurrencyType, Int>): MobBuilder {
        currencies = value
        return this
    }

    fun card(value: MobCardDAO?): MobBuilder {
        card = value
        return this
    }

    fun specialization(value: Specialization): MobBuilder {
        specialization = value
        return this
    }

    open fun build(): Mob {
        val mob = Mob(createMobArguments())
        mobService.addMob(mob)
        return mob
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
            card,
        )
    }
}
