package kotlinmud.test

import java.nio.channels.SocketChannel
import kotlinmud.action.ActionService
import kotlinmud.attributes.AttributesBuilder
import kotlinmud.event.Event
import kotlinmud.event.EventService
import kotlinmud.io.ClientService
import kotlinmud.io.IOStatus
import kotlinmud.io.NIOClient
import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.item.HasInventory
import kotlinmud.item.ItemService
import kotlinmud.item.model.Item
import kotlinmud.item.model.ItemBuilder
import kotlinmud.item.model.ItemOwner
import kotlinmud.item.type.Position
import kotlinmud.mob.MobController
import kotlinmud.mob.MobService
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.fight.Round
import kotlinmud.mob.model.Appetite
import kotlinmud.mob.model.Mob
import kotlinmud.mob.model.MobBuilder
import kotlinmud.mob.model.MobRoom
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.type.JobType
import kotlinmud.player.PlayerService
import kotlinmud.player.model.MobCard
import kotlinmud.player.model.MobCardBuilder
import kotlinmud.room.model.Room
import kotlinmud.service.FixtureService
import kotlinmud.service.RespawnService

class TestService(
    private val fixtureService: FixtureService,
    private val mobService: MobService,
    private val itemService: ItemService,
    private val actionService: ActionService,
    private val respawnService: RespawnService,
    private val eventService: EventService,
    private val playerService: PlayerService
) {
    private val clientService = ClientService()

    init {
        createItem(mobService.getStartRoom())
    }

    fun <T> publish(event: Event<T>) {
        eventService.publish(event)
    }

    fun createClient(): NIOClient {
        val client = NIOClient(SocketChannel.open())
        clientService.addClient(client)
        return client
    }

    fun createMobController(mob: Mob): MobController {
        return MobController(mobService, itemService, eventService, mob)
    }

    fun countItemsFor(hasInventory: HasInventory): Int {
        return itemService.findAllByOwner(hasInventory).size
    }

    fun getItemsFor(hasInventory: HasInventory): List<Item> {
        return itemService.findAllByOwner(hasInventory)
    }

    fun regenMobs() {
        mobService.regenMobs()
    }

    fun putMobInRoom(mob: Mob, room: Room) {
        mobService.putMobInRoom(mob, room)
    }

    fun respawnWorld() {
        respawnService.respawn()
    }

    fun getRooms(): List<Room> {
        return mobService.getRooms()
    }

    fun getStartRoom(): Room {
        return mobService.getStartRoom()
    }

    fun createMob(job: JobType = JobType.NONE): Mob {
        val mobBuilder = fixtureService.createMobBuilder()
        val mob = mobBuilder.job(job).build()
        mob.equipped.add(weapon(mob))
        mobService.addMob(mob)
        return mob
    }

    fun createCorpseFrom(mob: Mob): Item {
        return mobService.createCorpseFrom(mob)
    }

    fun createPlayerMobBuilder(): MobBuilder {
        val mob = fixtureService.createMobBuilder().isNpc(false)
        val name = fixtureService.faker.name.name()
        mob.name(name)
        playerService.addMobCard(
            MobCardBuilder()
                .mobName(name)
                .playerEmail("foo@bar.com")
                .experiencePerLevel(1000)
                .appetite(Appetite.fromRace(Human()))
                .build()
        )
        return mob
    }

    fun withMob(builder: (MobBuilder) -> MobBuilder): Mob {
        return buildMob(builder(fixtureService.createMobBuilder()))
    }

    fun findMobCardByName(name: String): MobCard? {
        return playerService.findMobCardByName(name)
    }

    fun createItem(hasInventory: HasInventory): Item {
        return buildItem(itemBuilder(), hasInventory)
    }

    fun itemBuilder(): ItemBuilder {
        return fixtureService.createItemBuilder()
    }

    fun buildItem(itemBuilder: ItemBuilder, hasInventory: HasInventory): Item {
        itemBuilder.build().let {
            itemService.add(ItemOwner(it, hasInventory))
            return it
        }
    }

    fun make(amount: Int): MakeItemService {
        return MakeItemService(this, amount)
    }

    fun getMobRooms(): List<MobRoom> {
        return mobService.getMobRooms()
    }

    fun getRoomForMob(mob: Mob): Room {
        return mobService.getRoomForMob(mob)
    }

    fun getMobsForRoom(room: Room): List<Mob> {
        return mobService.getMobsForRoom(room)
    }

    fun pruneDeadMobs() {
        mobService.pruneDeadMobs()
    }

    fun decrementDelays() {
        clientService.decrementDelays()
    }

    fun decrementAffects() {
        mobService.decrementAffects()
    }

    fun runAction(mob: Mob, input: String): Response {
        return actionService.run(Request(mob, input, mobService.getRoomForMob(mob)))
    }

    fun runActionForIOStatus(mob: Mob, input: String, status: IOStatus): Response {
        var i = 0
        while (i < 100) {
            val response = runAction(mob, input)
            if (response.status == status) {
                return response
            }
            i++
        }
        throw Exception("cannot generate desired IOStatus")
    }

    fun addFight(fight: Fight) {
        mobService.addFight(fight)
    }

    fun findFightForMob(mob: Mob): Fight? {
        return mobService.findFightForMob(mob)
    }

    fun proceedFights(): List<Round> {
        return mobService.proceedFights()
    }

    private fun buildMob(mobBuilder: MobBuilder): Mob {
        val mob = mobBuilder.build()
        mob.equipped.add(weapon(mob))
        mobService.addMob(mob)
        return mob
    }

    private fun weapon(hasInventory: HasInventory): Item {
        return buildItem(itemBuilder()
            .position(Position.WEAPON)
            .attributes(
                AttributesBuilder()
                    .hit(2)
                    .dam(1)
                    .build()
            ), hasInventory)
    }
}
