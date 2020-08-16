package kotlinmud.player.authStep.model

import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.specialization.type.Specialization
import kotlinmud.player.dao.MobCardDAO
import org.jetbrains.exposed.sql.transactions.transaction

class CreationFunnel(val email: String) {
    lateinit var name: String
    lateinit var race: Race
    lateinit var specialization: Specialization

    fun build(): MobDAO {
        return transaction {
            MobDAO.new {
                this.name = name
                isNpc = false
                mobCard = MobCardDAO.new {}
                this.player = player
            }
        }
    }
}
