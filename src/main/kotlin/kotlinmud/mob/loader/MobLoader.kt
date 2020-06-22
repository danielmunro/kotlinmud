package kotlinmud.mob.loader

import java.util.Random
import kotlinmud.affect.loader.AffectLoader
import kotlinmud.affect.model.AffectInstance
import kotlinmud.attributes.loader.AttributesLoader
import kotlinmud.fs.constant.CURRENT_LOAD_SCHEMA_VERSION
import kotlinmud.fs.loader.Tokenizer
import kotlinmud.fs.loader.area.loader.Loader
import kotlinmud.fs.loader.area.loader.intAttr
import kotlinmud.fs.loader.area.loader.parseAffects
import kotlinmud.fs.loader.area.loader.strAttr
import kotlinmud.mob.factory.mobBuilder
import kotlinmud.mob.model.MobBuilder
import kotlinmud.mob.race.factory.createRaceFromString
import kotlinmud.mob.skill.loader.SkillLoader
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.Gender
import kotlinmud.mob.type.JobType
import kotlinmud.mob.type.SpecializationType

class MobLoader(
    private val tokenizer: Tokenizer,
    private val loadSchemaVersion: Int = CURRENT_LOAD_SCHEMA_VERSION,
    private val isNpc: Boolean = true
) : Loader {
    override fun load(): MobBuilder {
        val id = tokenizer.parseInt()
        val name = tokenizer.parseString()
        val brief = tokenizer.parseString()
        val description = tokenizer.parseString()
        val disposition = Disposition.valueOf(tokenizer.parseString().toUpperCase())
        val hp = tokenizer.parseInt()
        val mana = tokenizer.parseInt()
        val mv = tokenizer.parseInt()
        val level = tokenizer.parseInt()
        val maxItems = tokenizer.parseInt()
        val maxWeight = tokenizer.parseInt()
        val wimpy = tokenizer.parseInt()
        val savingThrows = if (loadSchemaVersion >= 10) tokenizer.parseInt() else 0
        val gold = if (loadSchemaVersion >= 10) tokenizer.parseInt() else 0
        val race =
            createRaceFromString(if (loadSchemaVersion >= 10) tokenizer.parseString() else "human")
        val gender = Gender.valueOf(if (loadSchemaVersion >= 10) tokenizer.parseString() else "NONE")
        val skills = if (loadSchemaVersion >= 10) SkillLoader(tokenizer).load() else mutableMapOf()
        val attributes = AttributesLoader(tokenizer).load()
        val affects = if (loadSchemaVersion >= 10) AffectLoader(tokenizer).load().toMutableList() else mutableListOf()
        val props = tokenizer.parseProperties()
        val job = JobType.valueOf(strAttr(props["job"], "none").toUpperCase())
        val specialization = SpecializationType.valueOf(
            strAttr(
                props["specialization"],
                "none"
            ).toUpperCase())
        val goldMin = intAttr(props["goldMin"], 0)
        val goldMax = intAttr(props["goldMax"], 1)
        val builder = mobBuilder(id, name)
        if (loadSchemaVersion < 10) {
            parseAffects(tokenizer).forEach {
                affects.add(AffectInstance(it, 0))
            }
        }
        val strRoute = strAttr(props["route"])
        val route = if (strRoute != "") strRoute.split("-").map { it.toInt() } else listOf()

        return builder
            .id(id)
            .name(name)
            .brief(brief)
            .description(description)
            .disposition(disposition)
            .hp(hp)
            .mana(mana)
            .mv(mv)
            .level(level)
            .maxItems(maxItems)
            .maxWeight(maxWeight)
            .wimpy(wimpy)
            .job(job)
            .specialization(specialization)
            .race(race)
            .gender(gender)
            .savingThrows(savingThrows)
            .gold(if (isNpc) Random().nextInt(goldMax - goldMin) + goldMin else gold)
            .goldMin(goldMin)
            .goldMax(goldMax)
            .route(route)
            .isNpc(isNpc)
            .skills(skills.toMutableMap())
            .affects(affects)
            .attributes(attributes)
    }
}
