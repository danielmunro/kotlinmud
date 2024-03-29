package kotlinmud.player.auth.service

import kotlinmud.io.model.Client
import kotlinmud.mob.model.PlayerMob
import kotlinmud.mob.service.MobService
import kotlinmud.player.auth.model.CreationFunnel
import kotlinmud.player.dao.PlayerDAO
import kotlinmud.player.service.PlayerService
import kotlinmud.room.service.RoomService
import kotlinmud.player.repository.findPlayerByEmail as findPlayerByEmailQuery
import kotlinmud.player.repository.findPlayerByName as findPlayerByNameQuery

class AuthStepService(
    private val mobService: MobService,
    private val roomService: RoomService,
    private val playerService: PlayerService
) {
    private val creationFunnels = mutableListOf<CreationFunnel>()

    fun createCreationFunnel(name: String): CreationFunnel {
        return CreationFunnel(mobService, name)
    }

    fun addCreationFunnel(creationFunnel: CreationFunnel) {
        creationFunnel.mobRoom = roomService.getStartRoom()
        creationFunnels.add(creationFunnel)
    }

    fun getCreationFunnelForName(name: String): CreationFunnel {
        return creationFunnels.find { it.name == name }!!
    }

    fun findPlayerMobByName(name: String): PlayerMob? {
        return playerService.rehydratePlayerMob(name)
    }

    fun createPlayer(accountName: String): PlayerDAO {
        return playerService.createNewPlayerWithAccountName(accountName)
    }

    fun findPlayerByOTP(otp: String): PlayerDAO? {
        return playerService.findPlayerByOTP(otp)
    }

    fun findPlayerByEmail(email: String): PlayerDAO? {
        return findPlayerByEmailQuery(email)
    }

    fun findPlayerByName(name: String): PlayerDAO? {
        return findPlayerByNameQuery(name)
    }

    fun sendOTP(player: PlayerDAO) {
        playerService.sendOTP(player)
    }

    fun loginClientAsPlayer(client: Client, player: PlayerDAO) {
        playerService.loginClientAsPlayer(client, player)
    }
}
