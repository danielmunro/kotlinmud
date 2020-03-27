package kotlinmud.loader.loader

import kotlin.random.Random
import kotlinmud.affect.AffectInstance
import kotlinmud.attributes.Attribute
import kotlinmud.attributes.Attributes
import kotlinmud.loader.Tokenizer
import kotlinmud.mob.Disposition
import kotlinmud.mob.JobType
import kotlinmud.mob.MobBuilder
import kotlinmud.mob.SpecializationType
import kotlinmud.mob.race.createRaceFromString

class MobLoader(private val tokenizer: Tokenizer) : WithAttrLoader() {
    var id = 0
    var name = ""
    var brief = ""
    var description = ""
    var disposition = Disposition.STANDING
    override var props: Map<String, String> = mapOf()

    override fun load(): MobBuilder {
        id = tokenizer.parseInt()
        name = tokenizer.parseString()
        brief = tokenizer.parseString()
        description = tokenizer.parseString()
        disposition = Disposition.valueOf(tokenizer.parseString().toUpperCase())
        props = tokenizer.parseProperties()
        val job = JobType.valueOf(strAttr("job", "none").toUpperCase())
        val specialization = SpecializationType.valueOf(strAttr("specialization", "none").toUpperCase())
        val goldMin = intAttr("goldMin", 0)
        val goldMax = intAttr("goldMax", 1)
        parseAttributes()
        val builder = MobBuilder()
        val affects: MutableList<AffectInstance> = mutableListOf()
        parseAffectTypes(tokenizer).forEach {
            affects.add(AffectInstance(it, 0))
        }
        val strRoute = strAttr("route")
        val route = if (strRoute != "") strRoute.split("-").map { it.toInt() } else listOf()
        var attributes = Attributes.Builder()
            .setAttribute(Attribute.HIT, hit)
            .setAttribute(Attribute.DAM, dam)
            .setAttribute(Attribute.STR, str)
            .setAttribute(Attribute.INT, int)
            .setAttribute(Attribute.WIS, wis)
            .setAttribute(Attribute.DEX, dex)
            .setAttribute(Attribute.CON, con)
            .setAttribute(Attribute.AC_SLASH, acSlash)
            .setAttribute(Attribute.AC_PIERCE, acPierce)
            .setAttribute(Attribute.AC_BASH, acBash)
            .setAttribute(Attribute.AC_MAGIC, acMagic)
            .build()

        return builder
            .id(id)
            .name(name)
            .brief(brief)
            .description(description)
            .disposition(disposition)
            .hp(hp)
            .mana(mana)
            .mv(mv)
            .job(job)
            .specialization(specialization)
            .race(createRaceFromString(strAttr("race", "human")))
            .gold(Random.nextInt(goldMin, goldMax))
            .route(route)
            .isNpc(true)
            .affects(affects)
            .attributes(attributes)
    }
}
