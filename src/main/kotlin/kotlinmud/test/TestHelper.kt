package kotlinmud.test

import com.commit451.mailgun.SendMessageResponse
import io.mockk.every
import io.mockk.mockk
import kotlinmud.action.service.ActionService
import kotlinmud.app.createContainer
import kotlinmud.db.applySchema
import kotlinmud.db.createConnection
import kotlinmud.db.getTables
import kotlinmud.event.observer.type.Observers
import kotlinmud.event.service.EventService
import kotlinmud.helper.Noun
import kotlinmud.io.service.ServerService
import kotlinmud.item.service.ItemService
import kotlinmud.mob.service.MobService
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.service.EmailService
import kotlinmud.player.service.PlayerService
import kotlinmud.service.FixtureService
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

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
    val parent = createContainer(0)
    val container = Kodein {
        extend(parent)
        bind<EmailService>(overrides = true) with singleton {
            val mock = mockk<EmailService>()
            every { mock.sendEmail(request = any()) } returns SendMessageResponse()
            mock
        }
        bind<PlayerService>(overrides = true) with singleton { PlayerService(instance<EmailService>(), instance<EventService>()) }
    }
    val fix: FixtureService by container.instance<FixtureService>()
    val mob: MobService by container.instance<MobService>()
    val item: ItemService by container.instance<ItemService>()
    val act: ActionService by container.instance<ActionService>()
    val evt: EventService by container.instance<EventService>()
    val serverService: ServerService by container.instance<ServerService>()
    val observers: Observers by container.instance<Observers>()
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
