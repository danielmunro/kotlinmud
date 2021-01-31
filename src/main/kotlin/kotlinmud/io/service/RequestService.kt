package kotlinmud.io.service

import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.Disposition
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.repository.findRoomByMobId

class RequestService(val mobId: Int, private val mobService: MobService, val input: String) {
    val args = input.toLowerCase().split(' ')

    fun getMobCard(): MobCardDAO {
        return mobService.getMob(mobId).mobCard!!
    }

    fun getMob(): MobDAO {
        return mobService.getMob(mobId)
    }

    fun getRoom(): RoomDAO {
        return findRoomByMobId(mobId)
    }

    fun getCommand(): String {
        return args[0]
    }

    fun getSubject(): String {
        return if (args.size > 1) args[1] else ""
    }

    fun getDisposition(): Disposition {
        return getMob().disposition
    }
}
