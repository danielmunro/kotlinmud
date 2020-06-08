package kotlinmud.event.impl

import kotlinmud.io.model.Client
import kotlinmud.player.model.MobCard

class PlayerLoggedInEvent(val client: Client, val mobCard: MobCard)
