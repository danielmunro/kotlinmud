package kotlinmud.io.service

import kotlinmud.mob.model.Mob
import kotlinmud.mob.type.Disposition
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.room.model.Room

class RequestService(val mob: Mob, val input: String) {
    val args = input.toLowerCase().split(' ')

    fun getMobCard(): MobCardDAO {
        return mob.mobCard!!
    }

    fun getRoom(): Room {
        return mob.room
    }

    fun getCommand(): String {
        return args[0]
    }

    fun getSubject(): String {
        return if (args.size > 1) args[1] else ""
    }

    fun getDisposition(): Disposition {
        return mob.disposition
    }
}
