package kotlinmud.event.event

import kotlinmud.io.NIOClient
import kotlinmud.player.model.MobCard

class PlayerLoggedInEvent(val client: NIOClient, val mobCard: MobCard)
