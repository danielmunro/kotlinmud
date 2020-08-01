package kotlinmud.event.observer.impl

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.fight.Round
import kotlinmud.mob.service.MobService

class WimpyObserver(private val mobService: MobService) : Observer {
    override val eventType: EventType = EventType.FIGHT_ROUND

    override fun <T> processEvent(event: Event<T>) {
        with(event.subject as Round) {
            if (checkWimpy(this.defender)) {
                flee(this.defender)
            } else if (checkWimpy(this.attacker)) {
                flee(this.attacker)
            }
        }
    }

    private fun checkWimpy(mob: MobDAO): Boolean {
        return mob.wimpy > mob.hp && mob.room.getAllExits().isNotEmpty()
    }

    private fun flee(mob: MobDAO) {
        mobService.endFightFor(mob)
        mob.room.getAllExits().entries.random().let {
            mobService.sendMessageToRoom(MessageBuilder()
                .toActionCreator("you flee heading ${it.key.value}!")
                .toTarget("$mob flees heading ${it.key.value}!")
                .build(),
                mob.room,
                mob
            )
            mob.room = it.value
            mobService.sendMessageToRoom(
                MessageBuilder().toObservers("$mob arrives.").build(),
                it.value,
                mob
            )
        }
    }
}
