package kotlinmud.loader.loader

import kotlin.random.Random
import kotlinmud.attributes.Attribute
import kotlinmud.loader.Tokenizer
import kotlinmud.mob.Disposition
import kotlinmud.mob.JobType
import kotlinmud.mob.Mob
import kotlinmud.mob.SpecializationType
import kotlinmud.mob.race.createRaceFromString

class MobLoader(private val tokenizer: Tokenizer) : WithAttrLoader() {
    var id = 0
    var name = ""
    var brief = ""
    var description = ""
    var disposition = Disposition.STANDING
    override var props: Map<String, String> = mapOf()

    override fun load(): Mob.Builder {
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
        val builder = Mob.Builder(id, name)
        parseAffectTypes(tokenizer).forEach {
            builder.addAffect(it)
        }

        return builder
            .setBrief(brief)
            .setDescription(description)
            .setDisposition(disposition)
            .setHp(hp)
            .setMana(mana)
            .setMv(mv)
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
            .setJob(job)
            .setSpecialization(specialization)
            .setRace(createRaceFromString(strAttr("race", "human")))
            .setGold(Random.nextInt(goldMin, goldMax))
    }
}
