package kotlinmud.test.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinmud.action.service.ActionService
import kotlinmud.attributes.type.Attribute
import kotlinmud.biome.helper.createBiomes
import kotlinmud.event.impl.ClientConnectedEvent
import kotlinmud.event.impl.Event
import kotlinmud.event.observer.impl.client.ClientConnectedObserver
import kotlinmud.event.observer.impl.pulse.ProceedFightsPulseObserver
import kotlinmud.event.observer.impl.round.WimpyObserver
import kotlinmud.event.observer.impl.tick.DecreaseThirstAndHungerObserver
import kotlinmud.event.observer.impl.tick.DecrementItemDecayTimerObserver
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
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.HasInventory
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.mob.builder.MobBuilder
import kotlinmud.mob.builder.PlayerMobBuilder
import kotlinmud.mob.controller.MobController
import kotlinmud.mob.fight.Round
import kotlinmud.mob.model.Fight
import kotlinmud.mob.model.Mob
import kotlinmud.mob.model.PlayerMob
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.service.FightService
import kotlinmud.mob.service.MobService
import kotlinmud.mob.specialization.impl.Warrior
import kotlinmud.mob.type.Gender
import kotlinmud.mob.type.JobType
import kotlinmud.player.auth.model.CreationFunnel
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.auth.type.AuthStep
import kotlinmud.player.dao.PlayerDAO
import kotlinmud.player.service.PlayerService
import kotlinmud.quest.service.QuestService
import kotlinmud.quest.type.Quest
import kotlinmud.quest.type.QuestType
import kotlinmud.resource.service.ResourceService
import kotlinmud.room.builder.RoomBuilder
import kotlinmud.room.model.Door
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.DoorDisposition
import kotlinmud.room.type.RegenLevel
import kotlinmud.time.service.TimeService
import kotlinx.coroutines.runBlocking
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
    private val roomService: RoomService,
    private val timeService: TimeService,
) {
    private val clientService = ClientService()
    private val room = RoomBuilder(roomService)
        .name("start room")
        .description("tbd")
        .area(Area.Test)
        .build()
    private val client: Client = spyk(Client(mockk(relaxed = true)))
    private var mob: PlayerMob? = null
    private var target: Mob? = null
    private var player: PlayerDAO? = null

    init {
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
                roomService,
                BiomeService(width, length, createBiomes()),
                this
            ).also {
                runStateMachine(it)
            }
            return this
        }
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
        return MobController(mobService, eventService, mob)
    }

    fun findAllItemsByOwner(hasInventory: HasInventory): List<Item> {
        return hasInventory.items
    }

    fun findMobsInRoom(room: Room = getStartRoom()): List<Mob> {
        return mobService.findMobsInRoom(room)
    }

    suspend fun regenMobs() {
        mobService.regenMobs()
    }

    fun getStartRoom(): Room {
        return room
    }

    fun getStartRoom(modifier: (Room) -> Unit): Room {
        val room = getStartRoom()
        modifier(room)
        return room
    }

    fun createDoor(): Door {
        return Door(
            "a door",
            "a door",
            DoorDisposition.CLOSED,
            DoorDisposition.CLOSED,
        )
    }

    fun createMob(name: String = fixtureService.faker.name.name()): PlayerMob {
        val mob = PlayerMobBuilder(mobService).also {
            it.name = name
            it.race = Human()
            it.room = getStartRoom()
            it.job = JobType.NONE
            it.maxItems = 100
            it.maxWeight = 1000
        }.build()
        weapon(mob)
        if (this.mob == null) {
            this.mob = mob
        } else if (this.target == null) {
            this.target = mob
        }

        return mob
    }

    fun createPlayerMobBuilder(): PlayerMobBuilder {
        return PlayerMobBuilder(mobService).also {
            it.name = fixtureService.faker.name.name()
            it.race = Human()
            it.room = getStartRoom()
        }
    }

    fun createMobBuilder(): MobBuilder {
        return MobBuilder(mobService).also {
            it.name = fixtureService.faker.name.name()
            it.race = Human()
            it.room = getStartRoom()
        }
    }

    fun createShopkeeper(): Mob {
        return createMobBuilder().also {
            it.job = JobType.SHOPKEEPER
            it.maxItems = 1000
            it.maxWeight = 10000
        }.build()
    }

    fun createQuestGiver(): Mob {
        return createMobBuilder()
            .also { it.job = JobType.QUEST }
            .build()
    }

    fun createTrainer(): Mob {
        return createMobBuilder()
            .also { it.job = JobType.TRAINER }
            .build()
    }

    fun createMob(modifier: (Mob) -> Unit): Mob {
        return createMob().let {
            modifier(it)
            it
        }
    }

    fun getMob(): Mob {
        return mob!!
    }

    fun getTarget(): Mob {
        return target!!
    }

    fun createRoom(): Room {
        return RoomBuilder(roomService)
            .name("a test room")
            .description("this is a test room")
            .area(Area.LorimirForest)
            .isIndoors(false)
            .regenLevel(RegenLevel.NORMAL)
            .build()
    }

    fun createRoom(modifier: (Room) -> Unit): Room {
        return createRoom().let {
            modifier(it)
            it
        }
    }

    fun createRoomBuilder(): RoomBuilder {
        return RoomBuilder(roomService)
            .name("foo")
            .description("bar")
            .area(Area.Test)
    }

    fun findRoom(predicate: (Room) -> Boolean): Room? {
        return roomService.findOne(predicate)
    }

    fun createCorpseFrom(mob: Mob): Item {
        return mobService.createCorpseFrom(mob)
    }

    fun createPlayerMob(name: String = fixtureService.faker.name.name(), player: PlayerDAO = createPlayer("${fixtureService.faker.funnyName.name()}@foo.com")): PlayerMob {
        return createMob(name).also {
            it.emailAddress = player.email
            playerService.loginPlayerAsMob(player, it)
        }
    }

    fun createPlayerMob(mutator: (mob: PlayerMob) -> Unit): PlayerMob {
        val mob = createPlayerMob()
        mutator(mob)
        return mob
    }

    fun createPlayer(emailAddress: String): PlayerDAO {
        return authStepService.createPlayer(emailAddress).also {
            if (player == null) {
                player = it
            }
        }
    }

    fun createItemBuilder(): ItemBuilder {
        return ItemBuilder(itemService)
            .name(fixtureService.faker.cannabis.healthBenefits() + " with a " + fixtureService.faker.hipster.words())
            .description("a nice looking herb is here")
            .material(Material.ORGANIC)
            .type(ItemType.OTHER)
    }

    fun createItem(): Item {
        return createItemBuilder()
            .type(ItemType.FURNITURE)
            .material(Material.ORGANIC)
            .worth(0)
            .build()
    }

    fun createItem(modifier: (Item) -> Unit): Item {
        return createItem().let {
            modifier(it)
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
                it.gender = Gender.ANY
                it.specialization = Warrior()
            }
        )
    }

    fun runPreAuth(message: String): PreAuthResponse {
        return runBlocking { playerService.handlePreAuthRequest(PreAuthRequest(client, message)) }
    }

    fun runAction(input: String): Response {
        return runAction(mob ?: createMob(), input)
    }

    fun runActionForIOStatus(mob: PlayerMob, input: String, status: IOStatus, doBetween: () -> Unit = fun() {}): Response {
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
        runBlocking { ClientConnectedObserver(mobService, roomService, playerService).invokeAsync(event) }
    }

    fun callDecreaseThirstAndHungerEvent(event: Event<*>) {
        runBlocking { DecreaseThirstAndHungerObserver(serverService).invokeAsync(event) }
    }

    fun callGenerateGrassObserver() {
        runBlocking { GenerateGrassObserver(ResourceService(itemService, roomService)).invokeAsync(Event(EventType.TICK, null)) }
    }

    fun callDecayEvent() {
        runBlocking { DecrementItemDecayTimerObserver(mobService, roomService, itemService).invokeAsync(Event(EventType.TICK, null)) }
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

    fun runAction(mob: PlayerMob, input: String): Response {
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
            .type(ItemType.CONTAINER)
            .material(Material.TEXTILE)
            .build()
    }

    fun incrementTicks(amount: Int) {
        runBlocking {
            for (i in 0..amount) {
                timeService.tick()
            }
        }
    }

    private fun weapon(mob: Mob): Item {
        val item = ItemBuilder(itemService)
            .name("a sword")
            .description("a sword")
            .type(ItemType.EQUIPMENT)
            .position(Position.WEAPON)
            .attributes(
                mapOf(
                    Pair(Attribute.HIT, 2),
                    Pair(Attribute.DAM, 2),
                )
            )
            .material(Material.IRON)
            .build()
        mob.equipped.add(item)
        return item
    }
}
