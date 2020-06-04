package kotlinmud.test

import com.tylerthrailkill.helpers.prettyprint.pp
import java.lang.StringBuilder
import kotlinmud.Noun
import kotlinmud.action.ActionService
import kotlinmud.app.createContainer
import kotlinmud.event.EventService
import kotlinmud.event.observer.Observers
import kotlinmud.item.service.ItemService
import kotlinmud.mob.service.MobService
import kotlinmud.player.service.PlayerService
import kotlinmud.service.FixtureService
import kotlinmud.service.RespawnService
import org.kodein.di.erased.instance

fun createTestService(): TestService {
    val container = createContainer(0, true)
    val fix: FixtureService by container.instance()
    val mob: MobService by container.instance()
    val item: ItemService by container.instance()
    val act: ActionService by container.instance()
    val evt: EventService by container.instance()
    val respawnService: RespawnService by container.instance()
    val observers: Observers by container.instance()
    val playerService: PlayerService by container.instance()
    evt.observers = observers
    return TestService(
        fix,
        mob,
        item,
        act,
        respawnService,
        evt,
        playerService
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
