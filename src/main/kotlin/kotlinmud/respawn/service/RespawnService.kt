package kotlinmud.respawn.service

import kotlinmud.helper.logger

class RespawnService(
    private val itemAreaRespawnService: ItemAreaRespawnService,
    private val mobRespawnService: MobRespawnService,
    private val itemMobRespawnService: ItemMobRespawnService,
) {
    val logger = logger(this)

    suspend fun respawn() {
        logger.debug("respawn service called")
        itemAreaRespawnService.respawn()
        mobRespawnService.respawn()
        itemMobRespawnService.respawn()
    }
}
