package kotlinmud.mob.loader

import kotlin.random.Random
import kotlinmud.affect.AffectInstance
import kotlinmud.attributes.loader.AttributesLoader
import kotlinmud.attributes.model.Attributes
import kotlinmud.fs.loader.Tokenizer
import kotlinmud.fs.loader.area.loader.Loader
import kotlinmud.fs.loader.area.loader.intAttr
import kotlinmud.fs.loader.area.loader.parseAffects
import kotlinmud.fs.loader.area.loader.strAttr
import kotlinmud.mob.mobBuilder
import kotlinmud.mob.model.MobBuilder
import kotlinmud.mob.race.createRaceFromString
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.JobType
import kotlinmud.mob.type.SpecializationType
import kotlinmud.service.CURRENT_LOAD_SCHEMA_VERSION

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
        val attributesLoader = AttributesLoader(tokenizer)
        val attributes = if (loadSchemaVersion >= 7) attributesLoader.load() else Attributes()
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
        val affects: MutableList<AffectInstance> = mutableListOf()
        parseAffects(tokenizer).forEach {
            affects.add(AffectInstance(it, 0))
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
            .race(createRaceFromString(
                strAttr(
                    props["race"],
                    "human"
                )
            ))
            .gold(Random.nextInt(goldMin, goldMax))
            .goldMin(goldMin)
            .goldMax(goldMax)
            .route(route)
            .isNpc(isNpc)
            .affects(affects)
            .attributes(attributes)
    }
}
