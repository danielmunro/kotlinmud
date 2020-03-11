package kotlinmud.loader.mapper

import kotlinmud.loader.model.MobModel
import kotlinmud.mob.Mob

class MobMapper(private val mobs: List<MobModel>) {
    fun map(): List<Mob> {
        return mobs.map {
            Mob.Builder(it.id, it.name)
                .setBrief(it.brief)
                .setDescription(it.description)
                .setDisposition(it.disposition)
                .setHp(it.hp)
                .setMana(it.mana)
                .setMv(it.mv)
                .setLevel(it.level)
                .setRace(it.race)
                .setSpecialization(it.specialization)
                .setJob(it.job)
                .setGender(it.gender)
                .setGold(it.gold)
                .setWimpy(it.wimpy)
                .build()
        }
    }
}
