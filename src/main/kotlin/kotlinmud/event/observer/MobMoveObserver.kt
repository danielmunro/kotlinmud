package kotlinmud.event.observer

import kotlinmud.event.Event
import kotlinmud.event.EventType

class MobMoveObserver : Observer {
    override fun getEventTypes(): Array<EventType> {
        return arrayOf(EventType.MOB_MOVE)
    }

    override fun processEvent(event: Event) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}