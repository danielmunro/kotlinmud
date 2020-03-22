package kotlinmud

import kotlinmud.affect.AffectInstance
import kotlinmud.service.AffectService

interface Noun {
    val name: String
    val description: String
    val affects: MutableList<AffectInstance>

    fun affects(): AffectService
}
