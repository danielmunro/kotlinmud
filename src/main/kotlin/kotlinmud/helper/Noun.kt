package kotlinmud.helper

import kotlinmud.affect.model.AffectInstance
import kotlinmud.affect.service.AffectService

interface Noun {
    val name: String
    val description: String
    val affects: MutableList<AffectInstance>

    fun affects(): AffectService
}
