package kotlinmud.fs.loader.area.loader

import kotlin.random.Random
import kotlinmud.affect.AffectInstance
import kotlinmud.attributes.Attributes
import kotlinmud.fs.loader.Tokenizer
import kotlinmud.mob.MobBuilder
import kotlinmud.mob.mobBuilder
import kotlinmud.mob.race.createRaceFromString
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.JobType
import kotlinmud.mob.type.SpecializationType
import kotlinmud.service.CURRENT_LOAD_SCHEMA_VERSION

class MobLoader(
    private val tokenizer: Tokenizer,
    private val loadSchemaVersion: Int = CURRENT_LOAD_SCHEMA_VERSION,
    private val isNpc: Boolean = true
) : WithAttrLoader() {
    override var props: Map<String, String> = mapOf()

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
        if (loadSchemaVersion == 7) {
            tokenizer.parseString() // end
        }
        props = tokenizer.parseProperties()
        val job = JobType.valueOf(strAttr("job", "none").toUpperCase())
        val specialization = SpecializationType.valueOf(strAttr("specialization", "none").toUpperCase())
        val goldMin = intAttr("goldMin", 0)
        val goldMax = intAttr("goldMax", 1)
        parseAttributes()
        val builder = mobBuilder(id, name)
        val affects: MutableList<AffectInstance> = mutableListOf()
        parseAffectTypes(tokenizer).forEach {
            affects.add(AffectInstance(it, 0))
        }
        val strRoute = strAttr("route")
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
            .race(createRaceFromString(strAttr("race", "human")))
            .gold(Random.nextInt(goldMin, goldMax))
            .goldMin(goldMin)
            .goldMax(goldMax)
            .route(route)
            .isNpc(isNpc)
            .affects(affects)
            .attributes(attributes)
    }

    private fun loadTrainedAttributes(attributesLoader: AttributesLoader): MutableList<Attributes> {
        val trainedAttributes = mutableListOf<Attributes>()
        while (tokenizer.peek() != "end") {
            trainedAttributes.add(attributesLoader.load())
        }
        return trainedAttributes
    }
}
