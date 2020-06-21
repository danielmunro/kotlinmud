package kotlinmud.event.observer.impl

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.fight.Round
import kotlinmud.mob.model.Mob
import kotlinmud.mob.service.MobService

class WimpyObserver(private val mobService: MobService) : Observer {
    override val eventType: EventType = EventType.FIGHT_ROUND

    override fun <T> processEvent(event: Event<T>) {
        val round = event.subject as Round

        if (!checkWimpy(round.defender)) {
            checkWimpy(round.attacker)
        }
    }

    private fun checkWimpy(mob: Mob): Boolean {
        val room = mobService.getRoomForMob(mob)
        if (mob.wimpy > mob.hp && room.exits.size > 0) {
            mobService.endFightFor(mob)
            val exit = room.exits.random()
            mobService.sendMessageToRoom(MessageBuilder()
                .toActionCreator("you flee heading ${exit.direction.value}!")
                .toTarget("$mob flees heading ${exit.direction.value}!")
                .build(),
                room,
                mob
            )
            mobService.putMobInRoom(mob, exit.destination)
            mobService.sendMessageToRoom(
                MessageBuilder().toObservers("$mob arrives.").build(),
                exit.destination,
                mob
            )
            return true
        }
        return false
    }
}
