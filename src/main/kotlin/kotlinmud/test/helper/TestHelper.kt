package kotlinmud.test.helper

import kotlinmud.action.service.ActionService
import kotlinmud.app.createContainer
import kotlinmud.db.applySchema
import kotlinmud.db.createConnection
import kotlinmud.db.getTables
import kotlinmud.event.observer.type.ObserverList
import kotlinmud.event.service.EventService
import kotlinmud.generator.service.FixtureService
import kotlinmud.helper.Noun
import kotlinmud.io.service.ServerService
import kotlinmud.item.service.ItemService
import kotlinmud.mob.service.MobService
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.service.PlayerService
import kotlinmud.quest.service.QuestService
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.test.service.TestService
import kotlinmud.time.service.TimeService
import kotlinmud.world.createWorld
import kotlinmud.world.service.AreaBuilderService
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.direct
import org.kodein.di.erased.factory
import org.kodein.di.erased.instance

fun createTestService(): TestService {
    createConnection()
    transaction { SchemaUtils.drop(*getTables()) }
    applySchema()
    val container = createContainer(0, true)
    val fix: FixtureService by container.instance<FixtureService>()
    val mob: MobService by container.instance<MobService>()
    val item: ItemService by container.instance<ItemService>()
    val roomService: RoomService by container.instance<RoomService>()
    val areaBuilderServiceFactory = container.direct.factory<Area, AreaBuilderService>()
    createWorld(areaBuilderServiceFactory)
    val act: ActionService by container.instance<ActionService>()
    val evt: EventService by container.instance<EventService>()
    val serverService: ServerService by container.instance<ServerService>()
    val observers: ObserverList by container.instance<ObserverList>()
    val questService: QuestService by container.instance<QuestService>()
    val playerService: PlayerService by container.instance<PlayerService>()
    val authStepService: AuthStepService by container.instance<AuthStepService>()
    val timeService: TimeService by container.instance<TimeService>()
    playerService.setAuthStepService(authStepService)
    evt.observers = observers
    return TestService(
        fix,
        mob,
        item,
        act,
        evt,
        playerService,
        authStepService,
        serverService,
        questService,
        roomService,
        timeService,
    )
}

fun getIdentifyingWord(identifiable: Noun): String {
    return identifiable.name.split(" ").first { it.length > 3 }
}
