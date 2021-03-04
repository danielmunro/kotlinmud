package kotlinmud.app.containerModule

import kotlinmud.app.Tag
import kotlinmud.event.observer.impl.GuardAttacksAggroMobsObserver
import kotlinmud.event.observer.impl.LogOutAllPlayersOnStartupObserver
import kotlinmud.event.observer.impl.SendMessageToRoomObserver
import kotlinmud.event.observer.impl.SocialDistributorObserver
import kotlinmud.event.observer.impl.TillRoomObserver
import kotlinmud.event.observer.impl.client.ClientConnectedObserver
import kotlinmud.event.observer.impl.client.LogPlayerInObserver
import kotlinmud.event.observer.impl.client.LogPlayerOutObserver
import kotlinmud.event.observer.impl.gameLoop.ProcessClientBuffersObserver
import kotlinmud.event.observer.impl.gameLoop.ReadIntoClientBuffersObserver
import kotlinmud.event.observer.impl.gameLoop.RemoveDisconnectedClients
import kotlinmud.event.observer.impl.gameLoop.TimeServiceLoopObserver
import kotlinmud.event.observer.impl.kill.GrantExperienceOnKillObserver
import kotlinmud.event.observer.impl.kill.TransferGoldOnKillObserver
import kotlinmud.event.observer.impl.pulse.ProceedFightsPulseObserver
import kotlinmud.event.observer.impl.pulse.PruneDeadMobsPulseObserver
import kotlinmud.event.observer.impl.regen.FastHealingObserver
import kotlinmud.event.observer.impl.regen.MeditationObserver
import kotlinmud.event.observer.impl.round.EnhancedDamageObserver
import kotlinmud.event.observer.impl.round.SecondAttackObserver
import kotlinmud.event.observer.impl.round.ThirdAttackObserver
import kotlinmud.event.observer.impl.round.WimpyObserver
import kotlinmud.event.observer.impl.tick.ChangeWeatherObserver
import kotlinmud.event.observer.impl.tick.DecreaseThirstAndHungerObserver
import kotlinmud.event.observer.impl.tick.DecrementAffectTimeoutTickObserver
import kotlinmud.event.observer.impl.tick.DecrementDelayObserver
import kotlinmud.event.observer.impl.tick.DecrementItemDecayTimerObserver
import kotlinmud.event.observer.impl.tick.GenerateGrassObserver
import kotlinmud.event.observer.impl.tick.GenerateMobsObserver
import kotlinmud.event.observer.impl.tick.GrowResourcesObserver
import kotlinmud.event.observer.impl.tick.LogTickObserver
import kotlinmud.event.observer.impl.tick.MoveMobsOnTickObserver
import kotlinmud.event.observer.impl.tick.RegenMobsObserver
import kotlinmud.event.observer.impl.tick.RespawnObserver
import kotlinmud.event.observer.impl.tick.ScavengerCollectsItemsObserver
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.observer.type.ObserverList
import kotlinmud.event.type.EventType
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.provider
import org.kodein.di.erased.singleton

val ObserverModule = Kodein.Module {
    bind<Observer>(tag = Tag.LOGOUT_ALL_PLAYERS_ON_STARTUP) with provider {
        LogOutAllPlayersOnStartupObserver(instance())
    }

    bind<Observer>(tag = Tag.CLIENT_CONNECTED) with provider {
        ClientConnectedObserver(instance(), instance(), instance())
    }

    bind<Observer>(tag = Tag.SEND_MESSAGE_TO_ROOM) with provider {
        SendMessageToRoomObserver(instance(), instance())
    }

    bind<Observer>(tag = Tag.LOG_PLAYER_IN) with provider {
        LogPlayerInObserver(instance())
    }

    bind<Observer>(tag = Tag.LOG_PLAYER_OUT) with provider {
        LogPlayerOutObserver()
    }

    bind<Observer>(tag = Tag.SOCIAL) with provider {
        SocialDistributorObserver(instance(), instance())
    }

    bind<Observer>(tag = Tag.PROCEED_FIGHTS) with provider {
        ProceedFightsPulseObserver(instance())
    }

    bind<Observer>(tag = Tag.DECREMENT_AFFECT_TIMEOUT) with provider {
        DecrementAffectTimeoutTickObserver(instance())
    }

    bind<Observer>(tag = Tag.PRUNE_DEAD_MOBS) with provider {
        PruneDeadMobsPulseObserver(instance())
    }

    bind<Observer>(tag = Tag.DECREMENT_DELAY) with provider {
        DecrementDelayObserver(instance())
    }

    bind<Observer>(tag = Tag.DECREMENT_ITEM_DECAY_TIMER) with provider {
        DecrementItemDecayTimerObserver(instance())
    }

    bind<Observer>(tag = Tag.DECREASE_HUNGER_AND_THIRST) with provider {
        DecreaseThirstAndHungerObserver(instance(), instance())
    }

    bind<Observer>(tag = Tag.LOG_TICK) with provider {
        LogTickObserver(instance())
    }

    bind<Observer>(tag = Tag.CHANGE_WEATHER) with provider {
        ChangeWeatherObserver(instance())
    }

    bind<Observer>(tag = Tag.REGEN_MOBS) with provider {
        RegenMobsObserver(instance())
    }

    bind<Observer>(tag = Tag.MOVE_MOBS_ON_TICK) with provider {
        MoveMobsOnTickObserver(instance())
    }

    bind<Observer>(tag = Tag.SCAVENGER_COLLECTS_ITEM) with provider {
        ScavengerCollectsItemsObserver(instance(), instance(), instance())
    }

    bind<Observer>(tag = Tag.GENERATE_MOBS) with provider {
        GenerateMobsObserver(instance())
    }

    bind<Observer>(tag = Tag.FAST_HEALING) with provider {
        FastHealingObserver()
    }

    bind<Observer>(tag = Tag.MEDITATION) with provider {
        MeditationObserver()
    }

    bind<Observer>(tag = Tag.GUARD_ATTACKS_AGGRO_MOBS) with provider {
        GuardAttacksAggroMobsObserver(instance())
    }

    bind<Observer>(tag = Tag.WIMPY) with provider {
        WimpyObserver(instance())
    }

    bind<Observer>(tag = Tag.ENHANCED_DAMAGE) with provider {
        EnhancedDamageObserver()
    }

    bind<Observer>(tag = Tag.SECOND_ATTACK) with provider {
        SecondAttackObserver()
    }

    bind<Observer>(tag = Tag.THIRD_ATTACK) with provider {
        ThirdAttackObserver()
    }

    bind<Observer>(tag = Tag.GRANT_EXPERIENCE_ON_KILL) with provider {
        GrantExperienceOnKillObserver(instance())
    }

    bind<Observer>(tag = Tag.TRANSFER_GOLD_ON_KILL) with provider {
        TransferGoldOnKillObserver()
    }

    bind<Observer>(tag = Tag.GROW_RESOURCES) with provider {
        GrowResourcesObserver(instance())
    }

    bind<Observer>(tag = Tag.GENERATE_GRASS) with provider {
        GenerateGrassObserver(instance())
    }

    bind<Observer>(tag = Tag.PROCESS_CLIENT_BUFFERS) with provider {
        ProcessClientBuffersObserver(instance(), instance(), instance(), instance())
    }

    bind<Observer>(tag = Tag.READ_INTO_CLIENT_BUFFERS) with provider {
        ReadIntoClientBuffersObserver(instance())
    }

    bind<Observer>(tag = Tag.REMOVE_DISCONNECTED_CLIENTS) with provider {
        RemoveDisconnectedClients(instance())
    }

    bind<Observer>(tag = Tag.TIME_SERVICE_LOOP) with provider {
        TimeServiceLoopObserver(instance())
    }

    bind<Observer>(tag = Tag.TILL_ROOM) with provider {
        TillRoomObserver(instance())
    }

    bind<Observer>(tag = Tag.RESPAWN) with singleton {
        RespawnObserver(instance())
    }

    bind<ObserverList>() with singleton {
        mapOf(
            Pair(
                EventType.GAME_START,
                listOf(
                    instance(tag = Tag.LOGOUT_ALL_PLAYERS_ON_STARTUP)
                )
            ),
            Pair(
                EventType.GAME_LOOP,
                listOf(
                    instance(tag = Tag.PROCESS_CLIENT_BUFFERS),
                    instance(tag = Tag.READ_INTO_CLIENT_BUFFERS),
                    instance(tag = Tag.TIME_SERVICE_LOOP)
                )
            ),
            Pair(
                EventType.CLIENT_CONNECTED,
                listOf(
                    instance(tag = Tag.CLIENT_CONNECTED)
                )
            ),
            Pair(
                EventType.SEND_MESSAGE_TO_ROOM,
                listOf(
                    instance(tag = Tag.SEND_MESSAGE_TO_ROOM)
                )
            ),
            Pair(
                EventType.CLIENT_LOGGED_IN,
                listOf(
                    instance(tag = Tag.LOG_PLAYER_IN)
                )
            ),
            Pair(
                EventType.CLIENT_DISCONNECTED,
                listOf(
                    instance(tag = Tag.LOG_PLAYER_OUT)
                )
            ),
            Pair(
                EventType.SOCIAL,
                listOf(
                    instance(tag = Tag.SOCIAL)
                )
            ),
            Pair(
                EventType.PULSE,
                listOf(
                    instance(tag = Tag.PROCEED_FIGHTS),
                    instance(tag = Tag.DECREMENT_AFFECT_TIMEOUT),
                    instance(tag = Tag.PRUNE_DEAD_MOBS),
                    instance(tag = Tag.REMOVE_DISCONNECTED_CLIENTS)
                )
            ),
            Pair(
                EventType.TICK,
                listOf(
                    instance(tag = Tag.DECREMENT_DELAY),
                    instance(tag = Tag.DECREMENT_ITEM_DECAY_TIMER),
                    instance(tag = Tag.DECREASE_HUNGER_AND_THIRST),
                    instance(tag = Tag.LOG_TICK),
                    instance(tag = Tag.CHANGE_WEATHER),
                    instance(tag = Tag.REGEN_MOBS),
                    instance(tag = Tag.MOVE_MOBS_ON_TICK),
                    instance(tag = Tag.SCAVENGER_COLLECTS_ITEM),
//                    instance(tag = Tag.GENERATE_MOBS),
//                    instance(tag = Tag.GROW_RESOURCES),
//                    instance(tag = Tag.GENERATE_GRASS),
                    instance(tag = Tag.RESPAWN),
                )
            ),
            Pair(
                EventType.REGEN,
                listOf(
                    instance(tag = Tag.FAST_HEALING),
                    instance(tag = Tag.MEDITATION)
                )
            ),
            Pair(
                EventType.FIGHT_STARTED,
                listOf(
                    instance(tag = Tag.GUARD_ATTACKS_AGGRO_MOBS)
                )
            ),
            Pair(
                EventType.FIGHT_ROUND,
                listOf(
                    instance(tag = Tag.WIMPY),
                    instance(tag = Tag.ENHANCED_DAMAGE),
                    instance(tag = Tag.SECOND_ATTACK),
                    instance(tag = Tag.THIRD_ATTACK)
                )
            ),
            Pair(
                EventType.KILL,
                listOf(
                    instance(tag = Tag.GRANT_EXPERIENCE_ON_KILL),
                    instance(tag = Tag.TRANSFER_GOLD_ON_KILL)
                )
            ),
            Pair(
                EventType.TILL,
                listOf(
                    instance(tag = Tag.TILL_ROOM)
                )
            )
        )
    }
}
