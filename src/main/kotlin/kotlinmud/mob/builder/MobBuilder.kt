package kotlinmud.mob.builder

import kotlinmud.affect.model.Affect
import kotlinmud.attributes.constant.startingHp
import kotlinmud.attributes.constant.startingMana
import kotlinmud.attributes.constant.startingMv
import kotlinmud.attributes.type.Attribute
import kotlinmud.item.model.Item
import kotlinmud.item.type.HasInventory
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
import kotlinmud.mob.type.QuestGiver
import kotlinmud.mob.type.Rarity
import kotlinmud.quest.type.QuestType
import kotlinmud.resource.type.Resource
import kotlinmud.room.model.Room
import kotlinmud.type.Builder
import java.util.UUID

open class MobBuilder(private val mobService: MobService) : Builder, HasInventory {
    lateinit var name: String
    lateinit var brief: String
    lateinit var description: String
    lateinit var race: Race
    override var room: Room? = null
    var attributes = mutableMapOf(
        Pair(Attribute.HP, startingHp),
        Pair(Attribute.MANA, startingMana),
        Pair(Attribute.MV, startingMv),
    )
    var id = 0
    var job = JobType.FODDER
    var specialization: Specialization? = null
    var canonicalId: UUID = UUID.randomUUID()
    var identifier: QuestGiver? = null
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
    override var items = mutableListOf<Item>()
    var maxItems = 0
    var maxWeight = 0
    var route = listOf<Room>()
    var skills = mutableMapOf<SkillType, Int>()
    var affects = mutableListOf<Affect>()
    var currencies = mutableMapOf<CurrencyType, Int>()
    var randomizeRoom = true
    var messages = listOf<String>()
    var spellsForSale: List<Triple<SkillType, Int, Int>> = listOf()
    var resources = listOf<Resource>()
    var partOfQuest: QuestType? = null

    override fun setFromKeyword(keyword: String, value: String) {
        when (keyword) {
            "job" -> {
                job = JobType.valueOf(value.toUpperCase())
            }
        }
    }

    fun makeShopkeeper() {
        job = JobType.SHOPKEEPER
        currencies = mutableMapOf(
            Pair(CurrencyType.Gold, 100),
        )
    }

    override fun build(): Mob {
        return Mob(createMobArguments()).also { mobService.addMob(it) }
    }

    protected fun createMobArguments(): MobArguments {
        return MobArguments(
            id,
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
            identifier,
            attributes,
            room!!,
            equipped.toMutableList(),
            maxItems,
            maxWeight,
            items.toMutableList(),
            skills.toMutableMap(),
            affects.toMutableList(),
            currencies.toMutableMap(),
            route,
            messages,
            spellsForSale,
            resources,
            partOfQuest,
        )
    }
}
