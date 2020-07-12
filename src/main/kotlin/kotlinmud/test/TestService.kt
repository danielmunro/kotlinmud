package kotlinmud.test

import java.nio.channels.SocketChannel
import kotlinmud.action.service.ActionService
import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.model.startingHp
import kotlinmud.attributes.model.startingMana
import kotlinmud.attributes.model.startingMv
import kotlinmud.db.applySchema
import kotlinmud.db.createConnection
import kotlinmud.event.impl.Event
import kotlinmud.event.service.EventService
import kotlinmud.io.model.Client
import kotlinmud.io.model.Request
import kotlinmud.io.model.Response
import kotlinmud.io.service.ClientService
import kotlinmud.io.service.ServerService
import kotlinmud.io.type.IOStatus
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.HasInventory
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Position
import kotlinmud.mob.controller.MobController
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.fight.Round
import kotlinmud.mob.model.Appetite
import kotlinmud.mob.model.MobRoom
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.service.MobService
import kotlinmud.player.model.MobCard
import kotlinmud.player.model.MobCardBuilder
import kotlinmud.player.model.Player
import kotlinmud.player.service.PlayerService
import kotlinmud.room.dao.RoomDAO
import kotlinmud.service.FixtureService
import org.jetbrains.exposed.sql.transactions.transaction

class TestService(
    private val fixtureService: FixtureService,
    private val mobService: MobService,
    private val itemService: ItemService,
    private val actionService: ActionService,
    private val eventService: EventService,
    private val playerService: PlayerService,
    private val serverService: ServerService
) {
    private val clientService = ClientService()
    private val room: RoomDAO

    init {
        createConnection()
        applySchema()
        room = transaction {
            RoomDAO.new {
                name = "start room"
                description = "tbd"
                area = "midgaard"
            }
        }
        val item = createItem()
        transaction {
            room.items.plus(item)
        }
    }

    fun <T> publish(event: Event<T>) {
        eventService.publish(event)
    }

    fun readIntoBuffers() {
        serverService.readIntoBuffers()
    }

    fun createClient(): Client {
        val client = Client(SocketChannel.open())
        clientService.addClient(client)
        return client
    }

    fun createMobController(mob: MobDAO): MobController {
        return MobController(mobService, itemService, eventService, mob)
    }

    fun countItemsFor(hasInventory: HasInventory): Int {
        return itemService.findAllByOwner(hasInventory).size
    }

    fun countItemsFor(room: RoomDAO): Int {
        return itemService.findAllByRoom(room).size
    }

    fun getItemsFor(hasInventory: HasInventory): List<ItemDAO> {
        return itemService.findAllByOwner(hasInventory)
    }

    fun regenMobs() {
        mobService.regenMobs()
    }

    fun putMobInRoom(mob: MobDAO, room: RoomDAO) {
        mobService.putMobInRoom(mob, room)
    }

    fun respawnWorld() {
//        respawnService.respawn()
    }

    fun addNewRoom(mob: MobDAO) {
        mobService.createNewRoom(mob)
    }

    fun getRooms(): List<RoomDAO> {
        return mobService.getRooms()
    }

    fun getStartRoom(): RoomDAO {
        return room
    }

    fun createMob(): MobDAO {
        val mob = transaction {
            MobDAO.new {
                name = fixtureService.faker.name.name()
                description = "foo"
                brief = "bar"
                race = Human()
                isNpc = true
                hp = startingHp
                mana = startingMana
                mv = startingMv
                attributes = AttributesDAO.new {
                    hp = startingHp
                    mana = startingMana
                    mv = startingMana
                }
            }
        }
        transaction {
            mob.equipped.plus(weapon(mob))
        }
        putMobInRoom(mob, getStartRoom())

        return mob
    }

    fun createCorpseFrom(mob: MobDAO): ItemDAO {
        return mobService.createCorpseFrom(mob)
    }

    fun createPlayer(): Player {
        return playerService.createNewPlayerWithEmailAddress(fixtureService.faker.breakingBad.character() + "@hotmail.com")
    }

    fun createPlayerMob(): MobDAO {
        val mob = createMob()
        transaction { mob.isNpc = false }
        playerService.addMobCard(
            MobCardBuilder()
                .mobName(mob.name)
                .playerEmail("foo@bar.com")
                .experiencePerLevel(1000)
                .appetite(Appetite.fromRace(Human()))
                .build()
        )
        return mob
    }

    fun findPlayerByOTP(otp: String): Player? {
        return playerService.findPlayerByOTP(otp)
    }

    fun findMobCardByName(name: String): MobCard? {
        return playerService.findMobCardByName(name)
    }

    fun createItem(): ItemDAO {
        return transaction {
            ItemDAO.new {
                name = fixtureService.faker.cannabis.brands()
                description = "a nice looking herb is here"
                attributes = AttributesDAO.new {}
            }
        }
    }

    fun make(amount: Int): MakeItemService {
        return MakeItemService(amount)
    }

    fun getMobRooms(): List<MobRoom> {
        return mobService.getMobRooms()
    }

    fun getRoomForMob(mob: MobDAO): RoomDAO {
        return mobService.getRoomForMob(mob)
    }

    fun getMobsForRoom(room: RoomDAO): List<MobDAO> {
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

    fun runAction(mob: MobDAO, input: String): Response {
        return actionService.run(
            Request(
                mob,
                input,
                mobService.getRoomForMob(mob)
            )
        )
    }

    fun runActionForIOStatus(mob: MobDAO, input: String, status: IOStatus): Response {
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

    fun findFightForMob(mob: MobDAO): Fight? {
        return mobService.findFightForMob(mob)
    }

    fun proceedFights(): List<Round> {
        return mobService.proceedFights()
    }

    private fun weapon(mob: MobDAO): ItemDAO {
        return ItemDAO.new {
            name = "a sword"
            description = "a sword"
            type = ItemType.EQUIPMENT
            position = Position.WEAPON
            mobInventory = mob
            attributes = AttributesDAO.new {
                hit = 2
                dam = 1
            }
        }
    }
}
