package kotlinmud.loader.loader

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
    var disposition = Disposition.STANDING
    override var props: Map<String, String> = mapOf()

    override fun load(): Mob.Builder {
        id = tokenizer.parseInt()
        name = tokenizer.parseString()
        brief = tokenizer.parseString()
        description = tokenizer.parseString()
        disposition = Disposition.valueOf(tokenizer.parseString().toUpperCase())
        props = tokenizer.parseProperties()
        val hp = intAttr("hp")
        val mana = intAttr("mana")
        val mv = intAttr("mv")
        val job = JobType.valueOf(strAttr("job", "none").toUpperCase())
        val specialization = SpecializationType.valueOf(strAttr("specialization", "none").toUpperCase())

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
    }
}
