package kotlinmud.event.observer.impl

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.FightStartedEvent
import kotlinmud.event.observer.type.Observer
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.service.MobService
import kotlinmud.mob.table.Mobs
import kotlinmud.mob.type.JobType
import kotlinmud.room.dao.RoomDAO
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class GuardAttacksAggroMobsObserver(private val mobService: MobService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        val fight = event.subject as FightStartedEvent
        transaction {
            val room = fight.aggressor.room
            getMobsForRoomAndNotInFight(mobService, room, fight).forEach {
                mobService.addFight(it, fight.aggressor)
                runBlocking {
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
    }

    private fun getMobsForRoomAndNotInFight(mobService: MobService, room: RoomDAO, fight: FightStartedEvent): List<MobDAO> {
        return MobDAO.wrapRows(
            Mobs.select {
                Mobs.roomId eq room.id
            }
        ).filter {
            it != fight.aggressor &&
                    it != fight.defender &&
                    it.job == JobType.GUARD &&
                    mobService.getMobFight(it) == null
        }
    }
}
