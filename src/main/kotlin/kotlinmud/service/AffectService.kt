package kotlinmud.service

import kotlinmud.affect.Affect
import kotlinmud.affect.impl.Berserk

class AffectService {
    private val affects: List<Affect> = listOf(
        Berserk()
    )
}