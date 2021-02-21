package kotlinmud.mob.helper

import kotlinmud.attributes.constant.startingHp
import kotlinmud.attributes.constant.startingMana
import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.type.Gender
import kotlinmud.mob.type.JobType
import kotlinmud.mob.type.MobCanonicalId
import kotlinmud.room.dao.RoomDAO
import org.jetbrains.exposed.sql.transactions.transaction

class MobBuilder {
    private var name = ""
    private var brief = ""
    private var description = ""
    private var attributes: AttributesDAO? = null
    private var job: JobType? = null
    private var canonicalId: MobCanonicalId? = null
    private var level = 1
    private var hp = startingHp
    private var mana = startingMana
    private var mv = startingMana
    private var gender = Gender.ANY
    private lateinit var race: Race
    private lateinit var room: RoomDAO

    fun name(value: String): MobBuilder {
        name = value
        return this
    }

    fun brief(value: String): MobBuilder {
        brief = value
        return this
    }

    fun description(value: String): MobBuilder {
        description = value
        return this
    }

    fun room(value: RoomDAO): MobBuilder {
        room = value
        return this
    }

    fun attributes(value: AttributesDAO): MobBuilder {
        attributes = value
        return this
    }

    fun job(value: JobType): MobBuilder {
        job = value
        return this
    }

    fun race(value: Race): MobBuilder {
        race = value
        return this
    }

    fun canonicalId(value: MobCanonicalId): MobBuilder {
        canonicalId = value
        return this
    }

    fun level(value: Int): MobBuilder {
        level = value
        return this
    }

    fun vitals(hpValue: Int, manaValue: Int, mvValue: Int): MobBuilder {
        hp = hpValue
        mana = manaValue
        mv = mvValue
        return this
    }

    fun gender(value: Gender): MobBuilder {
        gender = value
        return this
    }

    fun build(): MobDAO {
        return transaction {
            MobDAO.new {
                this.name = this@MobBuilder.name
                this.brief = this@MobBuilder.brief
                this.description = this@MobBuilder.description
                this.level = this@MobBuilder.level
                this.room = this@MobBuilder.room
                this.job = this@MobBuilder.job
                this.race = this@MobBuilder.race
                this.canonicalId = this@MobBuilder.canonicalId
                this.hp = this@MobBuilder.hp
                this.mana = this@MobBuilder.mana
                this.mv = this@MobBuilder.mv
                this.gender = this@MobBuilder.gender
                this.attributes = this@MobBuilder.attributes ?: AttributesDAO.new {
                    hp = this@MobBuilder.hp
                    mana = this@MobBuilder.mana
                    mv = this@MobBuilder.mv
                }
            }
        }
    }
}
