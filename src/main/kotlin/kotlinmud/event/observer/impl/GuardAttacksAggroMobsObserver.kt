package kotlinmud.event.observer.impl

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.FightStartedEvent
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.service.MobService
import kotlinmud.mob.table.Mobs
import kotlinmud.mob.type.JobType
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class GuardAttacksAggroMobsObserver(private val mobService: MobService) :
    Observer {
    override val eventType: EventType = EventType.FIGHT_STARTED

    override fun <T> processEvent(event: Event<T>) {
        val fight = event.subject as FightStartedEvent
        val room = transaction { fight.aggressor.room }
        transaction {
            MobDAO.wrapRows(
                Mobs.select {
                    Mobs.roomId eq room.id
                }
            )
        }.filter {
            it != fight.aggressor &&
                    it != fight.defender &&
                    it.job == JobType.GUARD &&
                    mobService.findFightForMob(it) == null
        }.forEach {
            mobService.addFight(Fight(it, fight.aggressor))
            mobService.sendMessageToRoom(
                MessageBuilder()
                    .toActionCreator("You scream and attack ${fight.aggressor}!")
                    .toTarget("$it screams and attacks you!")
                    .toObservers("$it screams and attacks ${fight.aggressor}")
                    .build(),
                room,
                it,
                fight.aggressor
            )
        }
    }
}
