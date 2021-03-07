package kotlinmud.event.impl

import kotlinmud.io.model.Client
import kotlinmud.mob.model.PlayerMob

class PlayerLoggedInEvent(val client: Client, val mob: PlayerMob)
