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
        val round = event.subject as Round

        if (!checkWimpy(round.defender)) {
            checkWimpy(round.attacker)
        }
    }

    private fun checkWimpy(mob: MobDAO): Boolean {
        val room = mobService.getRoomForMob(mob)
        if (mob.wimpy > mob.hp && room.getAllExits().isNotEmpty()) {
            mobService.endFightFor(mob)
            val exit = room.getAllExits().entries.random()
            mobService.sendMessageToRoom(MessageBuilder()
                .toActionCreator("you flee heading ${exit.key.value}!")
                .toTarget("$mob flees heading ${exit.key.value}!")
                .build(),
                room,
                mob
            )
            mobService.putMobInRoom(mob, exit.value)
            mobService.sendMessageToRoom(
                MessageBuilder().toObservers("$mob arrives.").build(),
                exit.value,
                mob
            )
            return true
        }
        return false
    }
}
