package kotlinmud.respawn.service

class RespawnService(
    private val itemAreaRespawnService: ItemAreaRespawnService,
    private val mobRespawnService: MobRespawnService,
    private val itemMobRespawnService: ItemMobRespawnService,
) {
    suspend fun respawn() {
        itemAreaRespawnService.respawn()
        mobRespawnService.respawn()
        itemMobRespawnService.respawn()
    }
}
