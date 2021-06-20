package kotlinmud.mob.model

import kotlinmud.affect.model.Affect
import kotlinmud.attributes.type.Attribute
import kotlinmud.item.model.Item
import kotlinmud.mob.race.type.Race
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
import java.util.UUID

class MobArguments(
    val name: String,
    val brief: String,
    val description: String,
    var hp: Int,
    var mana: Int,
    var mv: Int,
    var level: Int,
    val race: Race,
    var disposition: Disposition,
    val job: JobType,
    val specialization: Specialization?,
    val gender: Gender,
    var wimpy: Int,
    val savingThrows: Int,
    val rarity: Rarity,
    val canonicalId: UUID?,
    val questGiver: QuestGiver?,
    val attributes: MutableMap<Attribute, Int>,
    var room: Room,
    val equipped: MutableList<Item>,
    val maxItems: Int,
    val maxWeight: Int,
    val items: MutableList<Item>,
    val skills: MutableMap<SkillType, Int>,
    val affects: MutableList<Affect>,
    val currencies: MutableMap<CurrencyType, Int>,
    val route: List<Room>?,
    val messages: List<String>,
    val spellsForSale: List<Triple<SkillType, Int, Int>>,
    val resources: List<Resource>,
    val partOfQuest: QuestType?,
)
