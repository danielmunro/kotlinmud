package kotlinmud.test

import kotlinmud.action.service.ActionService
import kotlinmud.app.createContainer
import kotlinmud.db.applySchema
import kotlinmud.db.createConnection
import kotlinmud.db.getTables
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.service.EventService
import kotlinmud.generator.service.FixtureService
import kotlinmud.helper.Noun
import kotlinmud.io.service.ServerService
import kotlinmud.item.service.ItemService
import kotlinmud.mob.service.MobService
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.service.PlayerService
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.erased.instance

fun createTestServiceWithResetDB(): TestService {
    createConnection()
    transaction {
        SchemaUtils.drop(*getTables())
        applySchema()
    }
    return createTestService()
}

fun createTestService(): TestService {
    createConnection()
    applySchema()
    val container = createContainer(0, true)
    val fix: FixtureService by container.instance<FixtureService>()
    val mob: MobService by container.instance<MobService>()
    val item: ItemService by container.instance<ItemService>()
    val act: ActionService by container.instance<ActionService>()
    val evt: EventService by container.instance<EventService>()
    val serverService: ServerService by container.instance<ServerService>()
    val observers: Observer by container.instance<Observer>()
    val playerService: PlayerService by container.instance<PlayerService>()
    val authStepService: AuthStepService by container.instance<AuthStepService>()
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
        serverService
    )
}

fun getIdentifyingWord(noun: Noun): String {
    return noun.name.split(" ").first { it.length > 3 }
}
