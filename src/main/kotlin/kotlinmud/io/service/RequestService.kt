package kotlinmud.io.service

import kotlinmud.mob.model.PlayerMob
import kotlinmud.mob.type.Disposition
import kotlinmud.room.model.Room

class RequestService(val mob: PlayerMob, val input: String) {
    val args = input.toLowerCase().split(' ')

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
