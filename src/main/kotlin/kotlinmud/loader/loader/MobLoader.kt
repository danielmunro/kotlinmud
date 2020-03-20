package kotlinmud.loader.loader

import kotlin.random.Random
import kotlinmud.loader.Tokenizer
import kotlinmud.mob.Disposition
import kotlinmud.mob.JobType
import kotlinmud.mob.Mob
import kotlinmud.mob.SpecializationType
import kotlinmud.mob.race.createRaceFromString

class MobLoader(private val tokenizer: Tokenizer) : Loader {
    var id = 0
    var name = ""
    var brief = ""
    var description = ""
    var hit = 0
    var dam = 0
    var hp = 0
    var mana = 0
    var mv = 0
    var str = 0
    var int = 0
    var wis = 0
    var dex = 0
    var con = 0
    var acSlash = 0
    var acBash = 0
    var acPierce = 0
    var acMagic = 0
    var disposition = Disposition.STANDING
    override var props: Map<String, String> = mapOf()

    override fun load(): Mob.Builder {
        id = tokenizer.parseInt()
        name = tokenizer.parseString()
        brief = tokenizer.parseString()
        description = tokenizer.parseString()
        disposition = Disposition.valueOf(tokenizer.parseString().toUpperCase())
        props = tokenizer.parseProperties()
        val ac = strAttr("ac", "0-0-0-0").split("-")
        hit = intAttr("hit")
        dam = intAttr("dam")
        hp = intAttr("hp")
        mana = intAttr("mana")
        mv = intAttr("mv")
        str = intAttr("str")
        int = intAttr("int")
        wis = intAttr("wis")
        dex = intAttr("dex")
        con = intAttr("con")
        acSlash = ac[0].toInt()
        acBash = ac[1].toInt()
        acPierce = ac[2].toInt()
        acMagic = ac[3].toInt()
        val job = JobType.valueOf(strAttr("job", "none").toUpperCase())
        val specialization = SpecializationType.valueOf(strAttr("specialization", "none").toUpperCase())
        val goldMin = intAttr("goldMin", 0)
        val goldMax = intAttr("goldMax", 1)

        return Mob.Builder(id, name)
            .setBrief(brief)
            .setDescription(description)
            .setDisposition(disposition)
            .setHp(hp)
            .setMana(mana)
            .setMv(mv)
            .setJob(job)
            .setSpecialization(specialization)
            .setRace(createRaceFromString(strAttr("race", "human")))
            .setGold(Random.nextInt(goldMin, goldMax))
    }
}
