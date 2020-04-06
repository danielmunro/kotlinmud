package kotlinmud.test

import kotlinmud.attributes.AttributesBuilder
import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.io.IOStatus
import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.item.HasInventory
import kotlinmud.item.Item
import kotlinmud.item.ItemBuilder
import kotlinmud.item.ItemOwner
import kotlinmud.item.Position
import kotlinmud.mob.JobType
import kotlinmud.mob.Mob
import kotlinmud.mob.MobBuilder
import kotlinmud.mob.MobController
import kotlinmud.mob.MobRoom
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.fight.Round
import kotlinmud.room.Room
import kotlinmud.service.ActionService
import kotlinmud.service.EventService
import kotlinmud.service.FixtureService
import kotlinmud.service.ItemService
import kotlinmud.service.MobService
import kotlinmud.service.RespawnService

class TestService(
    private val fixtureService: FixtureService,
    private val mobService: MobService,
    private val itemService: ItemService,
    private val actionService: ActionService,
    private val respawnService: RespawnService,
    private val eventService: EventService
) {
    init {
        createItem(mobService.getStartRoom())
    }

    fun <T, A> publish(event: Event<T>): EventResponse<A> {
        return eventService.publish(event)
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
        mobBuilder.job(job).build().let {
            it.equipped.items.add(weapon(it))
            mobService.addMob(it)
            return it
        }
    }

    fun createCorpseFrom(mob: Mob): Item {
        return mobService.createCorpseFrom(mob)
    }

    fun createPlayerMobBuilder(): MobBuilder {
        return fixtureService.createMobBuilder().isNpc(false)
    }

    fun withMob(builder: (MobBuilder) -> MobBuilder): Mob {
        return buildMob(builder(fixtureService.createMobBuilder()))
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
        mobService.decrementDelays()
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
        mob.equipped.items.add(weapon(mob))
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
