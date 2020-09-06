package kotlinmud.io.service

import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.Disposition
import kotlinmud.room.dao.RoomDAO

class RequestService(
    val mob: MobDAO,
    val mobId: Int,
    private val mobService: MobService,
    val input: String,
    val room: RoomDAO
) {
    val args: List<String> = input.toLowerCase().split(' ')

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
