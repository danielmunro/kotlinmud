package kotlinmud.fs.loader.area.loader

import kotlin.random.Random
import kotlinmud.affect.AffectInstance
import kotlinmud.attributes.Attribute
import kotlinmud.attributes.AttributeSetter
import kotlinmud.attributes.AttributesBuilder
import kotlinmud.fs.loader.Tokenizer
import kotlinmud.mob.Disposition
import kotlinmud.mob.JobType
import kotlinmud.mob.MobBuilder
import kotlinmud.mob.SpecializationType
import kotlinmud.mob.mobBuilder
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
        val builder = mobBuilder(id, name)
        val affects: MutableList<AffectInstance> = mutableListOf()
        parseAffectTypes(tokenizer).forEach {
            affects.add(AffectInstance(it, 0))
        }
        val strRoute = strAttr("route")
        val route = if (strRoute != "") strRoute.split("-").map { it.toInt() } else listOf()
        val attributes = AttributeSetter(AttributesBuilder())
            .set(Attribute.HIT, hit)
            .set(Attribute.DAM, dam)
            .set(Attribute.STR, str)
            .set(Attribute.INT, int)
            .set(Attribute.WIS, wis)
            .set(Attribute.DEX, dex)
            .set(Attribute.CON, con)
            .set(Attribute.AC_SLASH, acSlash)
            .set(Attribute.AC_PIERCE, acPierce)
            .set(Attribute.AC_BASH, acBash)
            .set(Attribute.AC_MAGIC, acMagic)
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
            .goldMin(goldMin)
            .goldMax(goldMax)
            .route(route)
            .isNpc(true)
            .affects(affects)
            .attributes(attributes)
    }
}
