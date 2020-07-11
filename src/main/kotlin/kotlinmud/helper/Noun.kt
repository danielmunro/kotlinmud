package kotlinmud.helper

import kotlinmud.affect.dao.AffectDAO
import org.jetbrains.exposed.sql.SizedIterable

interface Noun {
    val name: String
    val description: String
    val affects: SizedIterable<AffectDAO>
}
