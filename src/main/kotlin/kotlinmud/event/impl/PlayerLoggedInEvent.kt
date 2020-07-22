package kotlinmud.event.impl

import kotlinmud.io.model.Client
import kotlinmud.player.dao.MobCardDAO

class PlayerLoggedInEvent(val client: Client, val mobCard: MobCardDAO)
