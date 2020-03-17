package kotlinmud

import kotlinmud.affect.AffectInstance

interface Noun {
    val name: String
    val description: String
    val affects: MutableList<AffectInstance>
}
