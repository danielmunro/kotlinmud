package kotlinmud.test

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinmud.action.service.ActionService
import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.biome.helper.createBiomes
import kotlinmud.biome.type.BiomeType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.event.impl.ClientConnectedEvent
import kotlinmud.event.impl.Event
import kotlinmud.event.observer.impl.client.ClientConnectedObserver
import kotlinmud.event.observer.impl.logoutAllPlayersOnStartupEvent
import kotlinmud.event.observer.impl.pulse.ProceedFightsPulseObserver
import kotlinmud.event.observer.impl.round.WimpyObserver
import kotlinmud.event.observer.impl.tick.DecreaseThirstAndHungerObserver
import kotlinmud.event.observer.impl.tick.GenerateGrassObserver
import kotlinmud.event.service.EventService
import kotlinmud.event.type.EventType
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
import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.HasInventory
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.mob.controller.MobController
import kotlinmud.mob.fight.Round
import kotlinmud.mob.helper.MobBuilder
import kotlinmud.mob.model.Fight
import kotlinmud.mob.model.Mob
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.service.FightService
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.JobType
import kotlinmud.player.auth.model.CreationFunnel
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.auth.type.AuthStep
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.player.dao.PlayerDAO
import kotlinmud.player.service.PlayerService
import kotlinmud.quest.service.QuestService
import kotlinmud.quest.type.Quest
import kotlinmud.quest.type.QuestType
import kotlinmud.resource.service.ResourceService
import kotlinmud.room.dao.DoorDAO
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.repository.findStartRoom
import kotlinmud.room.type.Area
import kotlinmud.room.type.DoorDisposition
import kotlinmud.room.type.RegenLevel
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.transaction
import java.net.SocketAddress
import java.nio.channels.SocketChannel

class TestService(
    private val fixtureService: FixtureService,
    private val mobService: MobService,
    private val itemService: ItemService,
    private val actionService: ActionService,
    private val eventService: EventService,
    private val playerService: PlayerService,
    private val authStepService: AuthStepService,
    private val serverService: ServerService,
    private val questService: QuestService,
) {
    private val clientService = ClientService()
    private val room: RoomDAO
    private val client: Client = spyk(Client(mockk(relaxed = true)))
    private var mob: Mob? = null
    private var target: Mob? = null
    private var player: PlayerDAO? = null

    init {
        room = transaction {
            RoomDAO.new {
                name = "start room"
                description = "tbd"
                area = Area.LorimirForest
                biome = BiomeType.PLAINS
            }
        }
        every { client.socket.remoteAddress } returns mockk<SocketAddress>()
        serverService.getClients().add(client)
    }

    fun <T> publish(event: Event<T>) {
        runBlocking { eventService.publish(event) }
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
        runBlocking { serverService.readIntoBuffers() }
    }

    fun createClient(): Client {
        val client = Client(SocketChannel.open())
        clientService.addClient(client)
        return client
    }

    fun getClient(): Client {
        return client
    }

    fun loginClientAsPlayer(client: Client, player: PlayerDAO) {
        authStepService.loginClientAsPlayer(client, player)
        playerService.loginClientAsPlayer(client, player)
    }

    fun createMobController(mob: Mob): MobController {
        return MobController(mobService, itemService, eventService, mob)
    }

    fun countItemsFor(hasInventory: Any): Int {
        return if (hasInventory is ItemDAO) {
            transaction { hasInventory.items.count() }
        } else if (hasInventory is Mob) {
            hasInventory.items.size + hasInventory.equipped.size
        } else if (hasInventory is RoomDAO) {
            transaction { hasInventory.items.count() }
        } else {
            throw Exception()
        }
    }

    fun findAllItemsByOwner(hasInventory: HasInventory): List<Item> {
        return hasInventory.items
    }

    fun findMobsInRoom(room: RoomDAO = getStartRoom()): List<Mob> {
        return mobService.findMobsInRoom(room)
    }

    suspend fun regenMobs() {
        mobService.regenMobs()
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

    fun createMob(card: MobCardDAO? = null): Mob {
        val mob = MobBuilder(mobService)
            .name(card?.mobName ?: fixtureService.faker.name.name())
            .race(Human())
            .room(getStartRoom())
            .job(JobType.NONE)
            .card(card)
            .maxItems(100)
            .maxWeight(1000)
            .build()
        transaction {
            weapon(mob)
        }
        if (this.mob == null) {
            this.mob = mob
        } else if (this.target == null) {
            this.target = mob
        }

        return mob
    }

    fun createMobBuilder(): MobBuilder {
        return MobBuilder(mobService)
            .name(fixtureService.faker.name.name())
            .race(Human())
            .room(getStartRoom())
    }

    fun createShopkeeper(): Mob {
        return createMobBuilder()
            .job(JobType.SHOPKEEPER)
            .maxItems(1000)
            .maxWeight(10000)
            .build()
    }

    fun createQuestGiver(): Mob {
        return createMobBuilder()
            .job(JobType.QUEST)
            .build()
    }

    fun createMob(modifier: (Mob) -> Unit): Mob {
        return createMob().let {
            transaction { modifier(it) }
            it
        }
    }

    fun getMob(): Mob {
        return mob!!
    }

    fun getTarget(): Mob {
        return target!!
    }

    fun createRoom(): RoomDAO {
        return transaction {
            RoomDAO.new {
                name = "a test room"
                description = "this is a test room"
                area = Area.LorimirForest
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

    fun createCorpseFrom(mob: Mob): Item {
        return mobService.createCorpseFrom(mob)
    }

    fun createPlayerMob(name: String = fixtureService.faker.name.name(), player: PlayerDAO = createPlayer("${fixtureService.faker.funnyName.name()}@foo.com")): Mob {
        val race = Human()
        val maxAppetite = race.maxAppetite
        val maxThirst = race.maxThirst
        val card = transaction {
            MobCardDAO.new {
                mobName = name
                experiencePerLevel = 1000
                experience = 1000
                hunger = maxAppetite
                thirst = maxThirst
                respawnRoom = findStartRoom() ?: createRoom()
                this.player = player
            }
        }
        return createMob(card).also {
            playerService.loginPlayerAsMob(player, it)
        }
    }

    fun createPlayerMob(mutator: (mob: Mob) -> Unit): Mob {
        val mob = createPlayerMob()
        transaction { mutator(mob) }
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

    fun createItemBuilder(): ItemBuilder {
        return ItemBuilder(itemService)
                .name(fixtureService.faker.cannabis.healthBenefits() + " with a " + fixtureService.faker.hipster.words())
                .description("a nice looking herb is here")
    }

    fun createItem(): Item {
        return createItemBuilder().build()
    }

    fun createItem(modifier: (Item) -> Unit): Item {
        return createItem().let {
            transaction { modifier(it) }
            it
        }
    }

    fun createContainer(modifier: (Item) -> Unit): Item {
        return createContainer().let {
            transaction { modifier(it) }
            it
        }
    }

    fun createCreationFunnel(email: String): CreationFunnel {
        return CreationFunnel(mobService, email)
    }

    fun make(amount: Int): MakeItemService {
        return MakeItemService(itemService, amount)
    }

    suspend fun pruneDeadMobs() {
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
        authStepService.addCreationFunnel(
            CreationFunnel(mobService, player!!.email).also {
                it.mobName = "foo"
                it.mobRace = Human()
                it.mobRoom = getStartRoom()
            }
        )
    }

    fun runPreAuth(message: String): PreAuthResponse {
        return runBlocking { playerService.handlePreAuthRequest(PreAuthRequest(client, message)) }
    }

    fun runAction(input: String): Response {
        return runAction(mob ?: createMob(), input)
    }

    fun runActionForIOStatus(mob: Mob, input: String, status: IOStatus, doBetween: () -> Unit = fun() {}): Response {
        var i = 0
        while (i < 100) {
            val response = runAction(mob, input)
            if (response.status == status) {
                return response
            }
            doBetween()
            i++
        }
        throw Exception("cannot generate desired IOStatus")
    }

    fun addFight(mob1: Mob, mob2: Mob): FightService {
        return mobService.addFight(mob1, mob2)
    }

    fun findFightForMob(mob: Mob): Fight? {
        return mobService.getMobFight(mob)
    }

    fun proceedFights(): List<Round> {
        return runBlocking { mobService.proceedFights() }
    }

    fun callWimpyEvent(event: Event<*>) {
        runBlocking { WimpyObserver(mobService).invokeAsync(event) }
    }

    fun callClientConnectedEvent(event: Event<ClientConnectedEvent>) {
        runBlocking { ClientConnectedObserver(mobService, playerService).invokeAsync(event) }
    }

    fun callLogoutPlayersOnStartupEvent() {
        logoutAllPlayersOnStartupEvent(playerService)
    }

    fun callDecreaseThirstAndHungerEvent(event: Event<*>) {
        runBlocking { DecreaseThirstAndHungerObserver(serverService, mobService).invokeAsync(event) }
    }

    fun callGenerateGrassObserver() {
        runBlocking { GenerateGrassObserver(ResourceService(itemService)).invokeAsync(Event(EventType.TICK, null)) }
    }

    fun getAuthStep(client: Client): AuthStep? {
        return playerService.getAuthStepForClient(client)
    }

    fun callProceedFightsEvent() {
        runBlocking { ProceedFightsPulseObserver(mobService).invokeAsync(Event(EventType.PULSE, null)) }
    }

    fun findQuest(type: QuestType): Quest? {
        return questService.findByType(type)
    }

    fun runAction(mob: Mob, input: String): Response {
        return runBlocking {
            actionService.run(
                RequestService(
                    mob,
                    input
                )
            )
        }
    }

    fun createContainer(): Item {
        return createItemBuilder()
                .isContainer(true)
                .items(listOf())
                .maxItems(100)
                .maxWeight(1000)
                .build()
    }

    private fun weapon(mob: Mob): Item {
        val item = ItemBuilder(itemService)
            .name("a sword")
            .description("a sword")
            .type(ItemType.EQUIPMENT)
            .position(Position.WEAPON)
            .attributes(
                AttributesDAO.new {
                    hit = 2
                    dam = 1
                }
            )
            .material(Material.IRON)
            .build()
        mob.equipped.add(item)
        return item
    }
}
