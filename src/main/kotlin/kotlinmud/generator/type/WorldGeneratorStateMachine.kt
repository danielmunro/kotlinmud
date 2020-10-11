package kotlinmud.generator.type

import com.tinder.StateMachine
import kotlinmud.generator.statemachine.Event
import kotlinmud.generator.statemachine.SideEffect
import kotlinmud.generator.statemachine.State

typealias WorldGeneratorStateMachine = StateMachine<State, Event, SideEffect>
