package kotlinmud.mob.helper

import kotlinmud.affect.dao.AffectDAO
import kotlinmud.attributes.constant.startingHp
import kotlinmud.attributes.constant.startingMana
import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.item.dao.ItemDAO
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.model.Mob
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.service.MobService
import kotlinmud.mob.skill.dao.SkillDAO
import kotlinmud.mob.specialization.type.Specialization
import kotlinmud.mob.type.CurrencyType
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.Gender
import kotlinmud.mob.type.JobType
import kotlinmud.mob.type.MobCanonicalId
import kotlinmud.mob.type.Rarity
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.room.dao.RoomDAO
import org.jetbrains.exposed.sql.transactions.transaction

class MobBuilder (private val mobService: MobService) {
    private var name = ""
    private var brief = ""
    private var description = ""
    private var attributes: AttributesDAO? = null
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
    private var equipped = listOf<ItemDAO>()
    private var items = listOf<ItemDAO>()
    private var maxItems = 0
    private var maxWeight = 0
    private var skills = listOf<SkillDAO>()
    private var affects = listOf<AffectDAO>()
    private var currencies = mapOf<CurrencyType, Int>()
    private var card: MobCardDAO? = null
    private lateinit var race: Race
    private lateinit var room: RoomDAO

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

    fun room(value: RoomDAO): MobBuilder {
        room = value
        return this
    }

    fun attributes(value: AttributesDAO): MobBuilder {
        attributes = value
        return this
    }

    fun job(value: JobType): MobBuilder {
        job = value
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

    fun vitals(hpValue: Int, manaValue: Int, mvValue: Int): MobBuilder {
        hp = hpValue
        mana = manaValue
        mv = mvValue
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

    fun equipped(value: List<ItemDAO>): MobBuilder {
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

    fun items(value: List<ItemDAO>): MobBuilder {
        items = value
        return this
    }

    fun skills(value: List<SkillDAO>): MobBuilder {
        skills = value
        return this
    }

    fun affects(value: List<AffectDAO>): MobBuilder {
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

    fun fromDAO(mobDAO: MobDAO): MobBuilder {
        // todo do some stuff
        return this
    }

    fun build(): Mob {
        val mob = Mob(
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
            attributes ?: transaction { AttributesDAO.new {} },
            room,
            equipped.toMutableList(),
            maxItems,
            maxWeight,
            items.toMutableList(),
            skills.toMutableList(),
            affects.toMutableList(),
            currencies.toMutableMap(),
            card,
        )
        mobService.addMob(mob)
        return mob
    }
}
