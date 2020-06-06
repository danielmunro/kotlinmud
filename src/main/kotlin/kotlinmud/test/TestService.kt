package kotlinmud.test

import java.nio.channels.SocketChannel
import kotlinmud.action.service.ActionService
import kotlinmud.attributes.model.AttributesBuilder
import kotlinmud.event.Event
import kotlinmud.event.EventService
import kotlinmud.io.model.NIOClient
import kotlinmud.io.model.Request
import kotlinmud.io.model.Response
import kotlinmud.io.service.ClientService
import kotlinmud.io.type.IOStatus
import kotlinmud.item.model.Item
import kotlinmud.item.model.ItemBuilder
import kotlinmud.item.model.ItemOwner
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.HasInventory
import kotlinmud.item.type.Position
import kotlinmud.mob.controller.MobController
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.fight.Round
import kotlinmud.mob.model.Appetite
import kotlinmud.mob.model.Mob
import kotlinmud.mob.model.MobBuilder
import kotlinmud.mob.model.MobRoom
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.JobType
import kotlinmud.player.model.MobCard
import kotlinmud.player.model.MobCardBuilder
import kotlinmud.player.model.Player
import kotlinmud.player.service.PlayerService
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

    fun addNewRoom(mob: Mob) {
        mobService.createNewRoom(mob)
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

    fun createPlayer(): Player {
        return playerService.createNewPlayerWithEmailAddress(fixtureService.faker.breakingBad.character() + "@hotmail.com")
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

    fun addBuiltPlayerMob(mob: Mob) {
        mobService.addPlayerMob(mob)
    }

    fun withMob(builder: (MobBuilder) -> MobBuilder): Mob {
        return buildMob(builder(fixtureService.createMobBuilder()))
    }

    fun findPlayerByOTP(otp: String): Player? {
        return playerService.findPlayerByOTP(otp)
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
        return actionService.run(
            Request(
                mob,
                input,
                mobService.getRoomForMob(mob)
            )
        )
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
