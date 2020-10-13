package kotlinmud.test

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import java.net.SocketAddress
import java.nio.channels.SocketChannel
import kotlinmud.action.service.ActionService
import kotlinmud.attributes.constant.startingHp
import kotlinmud.attributes.constant.startingMana
import kotlinmud.attributes.constant.startingMv
import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.biome.helper.createBiomes
import kotlinmud.biome.type.BiomeType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.event.impl.ClientConnectedEvent
import kotlinmud.event.impl.Event
import kotlinmud.event.observer.impl.client.clientConnectedEvent
import kotlinmud.event.observer.impl.kill.GrantExperienceOnKillObserver
import kotlinmud.event.observer.impl.logoutAllPlayersOnStartupEvent
import kotlinmud.event.observer.impl.pulse.proceedFightsEvent
import kotlinmud.event.observer.impl.round.wimpyEvent
import kotlinmud.event.observer.impl.tick.decreaseThirstAndHungerEvent
import kotlinmud.event.service.EventService
import kotlinmud.generator.config.GeneratorConfig
import kotlinmud.generator.service.BiomeService
import kotlinmud.generator.service.FixtureService
import kotlinmud.generator.service.WorldGeneration
import kotlinmud.generator.statemachine.createStateMachine
import kotlinmud.generator.statemachine.runStateMachine
import kotlinmud.io.model.Client
import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.model.PreAuthResponse
import kotlinmud.io.model.Response
import kotlinmud.io.service.ClientService
import kotlinmud.io.service.RequestService
import kotlinmud.io.service.ServerService
import kotlinmud.io.type.IOStatus
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.HasInventory
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Position
import kotlinmud.mob.controller.MobController
import kotlinmud.mob.dao.FightDAO
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.fight.Round
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.service.FightService
import kotlinmud.mob.service.MobService
import kotlinmud.player.auth.model.CreationFunnel
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.auth.type.AuthStep
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.player.dao.PlayerDAO
import kotlinmud.player.service.PlayerService
import kotlinmud.room.dao.DoorDAO
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.repository.findStartRoom
import kotlinmud.room.type.DoorDisposition
import kotlinmud.room.type.RegenLevel
import org.jetbrains.exposed.sql.transactions.transaction

class TestService(
    private val fixtureService: FixtureService,
    private val mobService: MobService,
    private val itemService: ItemService,
    private val actionService: ActionService,
    private val eventService: EventService,
    private val playerService: PlayerService,
    private val authStepService: AuthStepService,
    private val serverService: ServerService
) {
    private val clientService = ClientService()
    private val room: RoomDAO
    private val client: Client = spyk(Client(mockk(relaxed = true)))
    private var mob: MobDAO? = null
    private var target: MobDAO? = null
    private var player: PlayerDAO? = null

    init {
        room = transaction {
            RoomDAO.new {
                name = "start room"
                description = "tbd"
                area = "midgaard"
                biome = BiomeType.PLAINS
            }
        }
//        createItem {
//            it.room = room
//        }
        every { client.socket.remoteAddress } returns mockk<SocketAddress>()
        serverService.getClients().add(client)
    }

    fun <T> publish(event: Event<T>) {
        eventService.publish(event)
    }

    fun createWorldGeneration(width: Int, length: Int): WorldGeneration {
        with(WorldGeneration()) {
            createStateMachine(
                GeneratorConfig(width, length),
                BiomeService(width, length, createBiomes()),
                this
            ).also {
                runStateMachine(it)
            }
            return this
        }
    }

    fun logOutPlayers() {
        playerService.logOutPlayers()
    }

    fun readIntoBuffers() {
        serverService.readIntoBuffers()
    }

    fun createClient(): Client {
        val client = Client(SocketChannel.open())
        clientService.addClient(client)
        return client
    }

    fun getClient(): Client {
        return client
    }

    fun getPlayer(): PlayerDAO? {
        return player
    }

    fun createMobController(mob: MobDAO): MobController {
        return MobController(mobService, itemService, eventService, mob)
    }

    fun countItemsFor(hasInventory: HasInventory): Int {
        return itemService.findAllByOwner(hasInventory).size
    }

    fun findAllItemsByOwner(hasInventory: HasInventory): List<ItemDAO> {
        return itemService.findAllByOwner(hasInventory)
    }

    fun regenMobs() {
        mobService.regenMobs()
    }

    fun putMobInRoom(mob: MobDAO, room: RoomDAO) {
        transaction { mob.room = room }
    }

    fun getStartRoom(): RoomDAO {
        return room
    }

    fun getStartRoom(modifier: (RoomDAO) -> Unit): RoomDAO {
        val room = getStartRoom()
        transaction {
            modifier(room)
        }
        return room
    }

    fun createDoor(): DoorDAO {
        return transaction {
            DoorDAO.new {
                name = "a door"
                description = "a door"
                disposition = DoorDisposition.CLOSED
                defaultDisposition = DoorDisposition.CLOSED
            }
        }
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
                room = getStartRoom()
                maxWeight = 100
                maxItems = 100
            }
        }
        transaction {
            weapon(mob)
        }
        putMobInRoom(mob, getStartRoom())
        if (this.mob == null) {
            this.mob = mob
        } else if (this.target == null) {
            this.target = mob
        }

        return mob
    }

    fun createMob(modifier: (MobDAO) -> Unit): MobDAO {
        return createMob().let {
            transaction { modifier(it) }
            it
        }
    }

    fun getMob(): MobDAO {
        return mobService.getMob(this.mob!!.id.value)
    }

    fun getTarget(): MobDAO {
        return mobService.getMob(this.target!!.id.value)
    }

    fun createRoom(): RoomDAO {
        return transaction {
            RoomDAO.new {
                name = "a test room"
                description = "this is a test room"
                area = "test"
                isIndoor = false
                regenLevel = RegenLevel.NORMAL
                biome = BiomeType.NONE
                substrate = SubstrateType.NONE
                elevation = 1
            }
        }
    }

    fun createRoom(modifier: (RoomDAO) -> Unit): RoomDAO {
        return createRoom().let {
            transaction { modifier(it) }
            it
        }
    }

    fun createCorpseFrom(mob: MobDAO): ItemDAO {
        return mobService.createCorpseFrom(mob)
    }

    fun createPlayerMob(): MobDAO {
        val mob = createMob()
        transaction {
            mob.isNpc = false
            mob.player = player
            val card = MobCardDAO.new {
                experiencePerLevel = 1000
                experience = 1000
                hunger = mob.race.maxAppetite
                thirst = mob.race.maxThirst
                this.mob = mob
                respawnRoom = findStartRoom()
            }
            mob.mobCard = card
        }
        return mob
    }

    fun createPlayer(emailAddress: String): PlayerDAO {
        return authStepService.createPlayer(emailAddress).also {
            if (player == null) {
                player = it
            }
        }
    }

    fun findMobCardByName(name: String): MobCardDAO? {
        return playerService.findMobCardByName(name)
    }

    fun createItem(): ItemDAO {
        return transaction {
            ItemDAO.new {
                name = fixtureService.faker.cannabis.healthBenefits() + " with a " + fixtureService.faker.hipster.words()
                description = "a nice looking herb is here"
                attributes = AttributesDAO.new {}
            }
        }
    }

    fun createItem(modifier: (ItemDAO) -> Unit): ItemDAO {
        return createItem().let {
            transaction { modifier(it) }
            it
        }
    }

    fun createContainer(modifier: (ItemDAO) -> Unit): ItemDAO {
        return createContainer().let {
            transaction { modifier(it) }
            it
        }
    }

    fun make(amount: Int): MakeItemService {
        return MakeItemService(amount)
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

    fun setPreAuth(builder: (AuthStepService, PlayerDAO) -> AuthStep) {
        playerService.setAuthStep(client, builder(authStepService, player!!))
        authStepService.addCreationFunnel(CreationFunnel(player!!.email).also {
            it.mobName = "foo"
            it.mobRace = Human()
            it.mobRoom = getStartRoom()
        })
    }

    fun runPreAuth(message: String): PreAuthResponse {
        return playerService.handlePreAuthRequest(PreAuthRequest(client, message))
    }

    fun runAction(input: String): Response {
        return runAction(mob ?: createMob(), input)
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

    fun addFight(mob1: MobDAO, mob2: MobDAO): FightService {
        return mobService.addFight(mob1, mob2)
    }

    fun findFightForMob(mob: MobDAO): FightDAO? {
        return mobService.getMobFight(mob)
    }

    fun proceedFights(): List<Round> {
        return mobService.proceedFights()
    }

    fun callWimpyEvent(event: Event<*>) {
        wimpyEvent(mobService, event)
    }

    fun callClientConnectedEvent(event: Event<ClientConnectedEvent>) {
        clientConnectedEvent(playerService, authStepService, event)
    }

    fun callLogoutPlayersOnStartupEvent() {
        logoutAllPlayersOnStartupEvent(playerService)
    }

    fun callDecreaseThirstAndHungerEvent(event: Event<*>) {
        decreaseThirstAndHungerEvent(serverService, mobService, event)
    }

    fun getGrantExperienceOnKillObserver(): GrantExperienceOnKillObserver {
        return GrantExperienceOnKillObserver(serverService)
    }

    fun getAuthStep(client: Client): AuthStep? {
        return playerService.getAuthStepForClient(client)
    }

    fun callProceedFightsEvent() {
        proceedFightsEvent(mobService)
    }

    fun flee(mob: MobDAO) {
        mobService.flee(mob)
    }

    private fun runAction(mob: MobDAO, input: String): Response {
        return actionService.run(
            RequestService(
                mob.id.value,
                mobService,
                input
            )
        )
    }

    private fun createContainer(): ItemDAO {
        val item = createItem()
        transaction { item.isContainer = true }
        return item
    }

    private fun weapon(mob: MobDAO): ItemDAO {
        return ItemDAO.new {
            name = "a sword"
            description = "a sword"
            type = ItemType.EQUIPMENT
            position = Position.WEAPON
            mobInventory = mob
            mobEquipped = mob
            attributes = AttributesDAO.new {
                hit = 2
                dam = 1
            }
        }
    }
}
