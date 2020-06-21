package kotlinmud.test

import com.tylerthrailkill.helpers.prettyprint.pp
import java.lang.StringBuilder
import kotlinmud.action.service.ActionService
import kotlinmud.app.createContainer
import kotlinmud.event.observer.type.Observers
import kotlinmud.event.service.EventService
import kotlinmud.helper.Noun
import kotlinmud.io.service.ServerService
import kotlinmud.item.service.ItemService
import kotlinmud.mob.service.MobService
import kotlinmud.player.service.PlayerService
import kotlinmud.service.FixtureService
import kotlinmud.service.RespawnService
import org.kodein.di.erased.instance

fun createTestService(): TestService {
    val container = createContainer(0, true)
    val fix: FixtureService by container.instance<FixtureService>()
    val mob: MobService by container.instance<MobService>()
    val item: ItemService by container.instance<ItemService>()
    val act: ActionService by container.instance<ActionService>()
    val evt: EventService by container.instance<EventService>()
    val respawnService: RespawnService by container.instance<RespawnService>()
    val serverService: ServerService by container.instance<ServerService>()
    val observers: Observers by container.instance<Observers>()
    val playerService: PlayerService by container.instance<PlayerService>()
    evt.observers = observers
    println("obs: ${observers.size}")
    return TestService(
        fix,
        mob,
        item,
        act,
        respawnService,
        evt,
        playerService,
        serverService
    )
}

fun getIdentifyingWord(noun: Noun): String {
    return noun.name.split(" ").first { it.length > 3 }
}

fun buf(toDump: Any): String {
    val buffer = StringBuilder()
    pp(toDump, 2, buffer, 80)
    return buffer.toString()
}
