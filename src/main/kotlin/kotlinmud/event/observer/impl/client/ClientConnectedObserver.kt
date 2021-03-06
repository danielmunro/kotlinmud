package kotlinmud.event.observer.impl.client

import kotlinmud.app.Environment
import kotlinmud.event.impl.ClientConnectedEvent
import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.Material
import kotlinmud.item.type.Weapon
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.service.MobService
import kotlinmud.mob.specialization.impl.Warrior
import kotlinmud.mob.type.CurrencyType
import kotlinmud.mob.type.Gender
import kotlinmud.player.auth.model.CreationFunnel
import kotlinmud.player.dao.PlayerDAO
import kotlinmud.player.repository.findPlayerByEmail
import kotlinmud.player.service.PlayerService
import kotlinmud.room.service.RoomService
import org.jetbrains.exposed.sql.transactions.transaction

class ClientConnectedObserver(
    private val mobService: MobService,
    private val roomService: RoomService,
    private val itemService: ItemService,
    private val playerService: PlayerService,
) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        with(event.subject as ClientConnectedEvent) {
            if (Environment.isDev()) {
                val player = findPlayerByEmail("dan@danmunro.com") ?: transaction {
                    PlayerDAO.new {
                        email = "dan@danmunro.com"
                        name = "foo"
                    }
                }
                playerService.loginClientAsPlayer(this.client, player)
                val funnel = CreationFunnel(mobService, "dan@danmunro.com")
                funnel.mobName = "foo"
                funnel.mobRace = Human()
                funnel.gender = Gender.ANY
                funnel.mobRoom = roomService.findOne { it.label == "fountain" }!!
//                funnel.mobRoom = roomService.getStartRoom()
                funnel.specialization = Warrior()
                val mob = funnel.build(player)
                mob.experienceToLevel = 1200
                mob.equipped.add(
                    itemService.builder(
                        "a sub-issue sword",
                        "tbd",
                        1.0,
                    ).also {
                        it.makeWeapon(
                            Weapon.SWORD,
                            DamageType.SLASH,
                            "slash",
                            Material.WOOD,
                            20,
                            20,
                        )
                    }.build()
                )
                mob.addCurrency(CurrencyType.Silver, 20)
                this.client.mob = mob
                return
            }
            playerService.addPreAuthClient(this.client)
            this.client.write("email: ")
        }
    }
}
