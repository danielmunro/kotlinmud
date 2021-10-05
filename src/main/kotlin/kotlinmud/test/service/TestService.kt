package kotlinmud.test.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinmud.action.service.ActionService
import kotlinmud.biome.helper.createBiomes
import kotlinmud.event.factory.createClientDisconnectedEvent
import kotlinmud.event.impl.ClientConnectedEvent
import kotlinmud.event.impl.Event
import kotlinmud.event.observer.impl.client.ClientConnectedObserver
import kotlinmud.event.observer.impl.client.LogPlayerOutObserver
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
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Weapon
import kotlinmud.mob.builder.MobBuilder
import kotlinmud.mob.builder.PlayerMobBuilder
import kotlinmud.mob.controller.MobController
import kotlinmud.mob.fight.Round
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.model.Fight
import kotlinmud.mob.model.Mob
import kotlinmud.mob.model.PlayerMob
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.service.FightService
import kotlinmud.mob.service.MobService
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.specialization.impl.Warrior
import kotlinmud.mob.type.CurrencyType
import kotlinmud.mob.type.Gender
import kotlinmud.mob.type.JobType
import kotlinmud.player.auth.model.CreationFunnel
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.auth.type.AuthStep
import kotlinmud.player.dao.PlayerDAO
import kotlinmud.player.service.PlayerService
import kotlinmud.quest.model.Quest
import kotlinmud.quest.service.QuestService
import kotlinmud.quest.type.QuestType
import kotlinmud.resource.service.ResourceService
import kotlinmud.room.builder.RoomBuilder
import kotlinmud.room.model.Door
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.DoorDisposition
import kotlinmud.room.type.RegenLevel
import kotlinmud.startup.service.StartupService
import kotlinmud.time.service.TimeService
import kotlinmud.world.service.AreaBuilderService
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.transaction
import java.net.SocketAddress
import java.nio.channels.SocketChannel
import java.util.UUID

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
    val testEmailAddress = "foo@bar.com"
    private val clientService = ClientService()
    private val room = RoomBuilder(roomService).also {
        it.name = "start room"
        it.description = "tbd"
        it.area = Area.Test
    }.build()
    private val client: Client = spyk(Client(mockk(relaxed = true)))
    private var mob: PlayerMob? = null
    private var target: Mob? = null
    private var player: PlayerDAO? = null

    init {
        every { client.socket.remoteAddress } returns mockk<SocketAddress>()
        serverService.getClients().add(client)
    }

    fun createStartupService(data: List<String>): StartupService {
        return StartupService(
            roomService,
            mobService,
            itemService,
            data,
        )
    }

    fun createAreaBuilderService(): AreaBuilderService {
        return AreaBuilderService(
            mobService,
            roomService,
            itemService,
            Area.Test,
        )
    }

    fun getTimeService(): TimeService {
        return timeService
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

    fun findMobsInRoom(room: Room = getStartRoom()): List<Mob> {
        return mobService.findMobsInRoom(room)
    }

    fun findPlayerMob(name: String): PlayerMob? {
        return mobService.findMob { it is PlayerMob && it.name == name } as PlayerMob?
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
            UUID.randomUUID(),
        )
    }

    fun createMob(name: String = fixtureService.faker.name.name()): PlayerMob {
        val mob = PlayerMobBuilder(mobService).also {
            it.name = name
            it.brief = name
            it.description = "(no description)"
            it.race = Human()
            it.room = getStartRoom()
            it.job = JobType.NONE
            it.maxItems = 100
            it.maxWeight = 1000
        }.build()
        weapon(mob)
        setMobOrTarget(mob)
        return mob
    }

    fun createPlayerMobBuilder(): PlayerMobBuilder {
        val name = fixtureService.faker.name.name()
        return PlayerMobBuilder(mobService).also {
            it.name = name
            it.brief = "$name is here"
            it.description = "$name is here"
            it.race = Human()
            it.room = getStartRoom()
        }
    }

    fun createMobBuilder(): MobBuilder {
        val name = fixtureService.faker.name.name()
        return mobService.builder(
            name,
            "$name is here",
            "$name is here",
        ).also {
            it.room = getStartRoom()
        }
    }

    fun createShopkeeper(): Mob {
        return createMobBuilder().also {
            it.job = JobType.SHOPKEEPER
            it.maxItems = 1000
            it.maxWeight = 10000
            it.currencies = mutableMapOf(
                Pair(CurrencyType.Gold, 1)
            )
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

    fun dumpPlayerMob(mob: PlayerMob) {
        return playerService.dumpPlayerMobData(mob)
    }

    fun hydratePlayerMob(name: String): PlayerMob? {
        return playerService.rehydratePlayerMob(name)
    }

    fun createRoom(): Room {
        return RoomBuilder(roomService).also {
            it.name = "a test room"
            it.description = "this is a test room"
            it.area = Area.Test
            it.isIndoors = false
            it.regenLevel = RegenLevel.NORMAL
        }.build()
    }

    fun createRoom(modifier: (Room) -> Unit): Room {
        return createRoom().let {
            modifier(it)
            it
        }
    }

    fun createRoomBuilder(): RoomBuilder {
        return RoomBuilder(roomService).also {
            it.name = "foo"
            it.description = "bar"
            it.area = Area.Test
        }
    }

    fun findMob(predicate: (Mob) -> Boolean): Mob? {
        return mobService.findMob(predicate)
    }

    fun findItem(predicate: (Item) -> Boolean): Item? {
        return itemService.findOne(predicate)
    }

    fun findRoom(predicate: (Room) -> Boolean): Room? {
        return roomService.findOne(predicate)
    }

    fun createCorpseFrom(mob: Mob): Item {
        return mobService.createCorpseFrom(mob)
    }

    fun createPlayerMob(name: String = fixtureService.faker.name.name(), player: PlayerDAO = createPlayer("${fixtureService.faker.funnyName.name()}@foo.com")): PlayerMob {
        return createMob(name).also {
            it.accountName = player.name
            playerService.loginPlayerAsMob(player, it)
        }
    }

    fun createPlayerMob(mutator: (mob: PlayerMob) -> Unit): PlayerMob {
        return createPlayerMob().also { mutator(it) }
    }

    fun createPlayer(accountName: String): PlayerDAO {
        return authStepService.createPlayer(accountName).also {
            transaction { it.email = testEmailAddress }
            if (player == null) {
                player = it
            }
        }
    }

    fun createItemBuilder(): ItemBuilder {
        return itemService.builder(
            fixtureService.faker.cannabis.healthBenefits() + " with a " + fixtureService.faker.hipster.words(),
            "a nice looking herb is here"
        ).also {
            it.material = Material.ORGANIC
            it.type = ItemType.OTHER
        }
    }

    fun createItem(): Item {
        return createItemBuilder().also {
            it.type = ItemType.FURNITURE
            it.material = Material.ORGANIC
        }.build()
    }

    fun createPotion(): Item {
        return createItemBuilder().also {
            it.type = ItemType.POTION
            it.material = Material.ORGANIC
            it.spells = listOf(SkillType.CURE_LIGHT)
        }.build()
    }

    fun createCreationFunnel(name: String): CreationFunnel {
        return CreationFunnel(mobService, name)
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
            CreationFunnel(mobService, player!!.name).also {
                it.mobName = "foo"
                it.email = testEmailAddress
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
        runBlocking { ClientConnectedObserver(playerService).invokeAsync(event) }
    }

    fun callDecreaseThirstAndHungerEvent(event: Event<*>) {
        runBlocking { DecreaseThirstAndHungerObserver(serverService).invokeAsync(event) }
    }

    fun callLogPlayerOutObserver(client: Client) {
        runBlocking { LogPlayerOutObserver(mobService).invokeAsync(createClientDisconnectedEvent(client)) }
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
        return createItemBuilder().also {
            it.makeContainer()
            it.material = Material.TEXTILE
        }.build()
    }

    fun incrementTicks(amount: Int) {
        runBlocking {
            for (i in 0..amount) {
                timeService.tick()
            }
        }
    }

    private fun setMobOrTarget(mob: PlayerMob) {
        if (this.mob == null) {
            this.mob = mob
        } else if (this.target == null) {
            this.target = mob
        }
    }

    private fun weapon(mob: Mob): Item {
        val item = itemService.builder(
            "a sword",
            "a sword"
        ).makeWeapon(
            Weapon.SWORD,
            DamageType.SLASH,
            "slash",
            Material.IRON,
            2,
            2,
        ).build()
        mob.equipped.add(item)
        return item
    }
}
