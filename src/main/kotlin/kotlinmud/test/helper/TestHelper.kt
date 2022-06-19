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
import kotlinmud.helper.getAllDataFiles
import kotlinmud.io.service.ServerService
import kotlinmud.item.service.ItemService
import kotlinmud.mob.service.MobService
import kotlinmud.persistence.service.StartupService
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.service.PlayerService
import kotlinmud.quest.service.QuestService
import kotlinmud.room.service.RoomService
import kotlinmud.test.service.TestService
import kotlinmud.time.service.TimeService
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.erased.instance

fun createTestService(hydrate: Boolean = true): TestService {
    createConnection()
    transaction { SchemaUtils.drop(*getTables()) }
    applySchema()
    val container = createContainer(0, true)
    val fix: FixtureService by container.instance<FixtureService>()
    val mob: MobService by container.instance<MobService>()
    val item: ItemService by container.instance<ItemService>()
    val roomService: RoomService by container.instance<RoomService>()
    val act: ActionService by container.instance<ActionService>()
    val evt: EventService by container.instance<EventService>()
    val serverService: ServerService by container.instance<ServerService>()
    val observers: ObserverList by container.instance<ObserverList>()
    val questService: QuestService by container.instance<QuestService>()
    val playerService: PlayerService by container.instance<PlayerService>()
    val authStepService: AuthStepService by container.instance<AuthStepService>()
    val timeService: TimeService by container.instance<TimeService>()
    val mobService: MobService by container.instance()
    val itemService: ItemService by container.instance()
    playerService.setAuthStepService(authStepService)
    evt.observers = observers
    if (hydrate) {
        val data = getAllDataFiles()
        val svc = StartupService(roomService, mobService, itemService, data)
        svc.hydrateWorld()
    }
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
