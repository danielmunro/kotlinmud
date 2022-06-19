package kotlinmud.persistence.spec

import kotlinmud.persistence.model.builder.Builder
import kotlinmud.persistence.model.builder.DoorModelBuilder
import kotlinmud.persistence.token.BriefToken
import kotlinmud.persistence.token.DescriptionToken
import kotlinmud.persistence.token.IdToken
import kotlinmud.persistence.token.NameToken
import kotlinmud.persistence.token.PropsToken

class DoorSpec : Spec {
    override val tokens = listOf(
        IdToken(),
        NameToken(),
        BriefToken(),
        DescriptionToken(),
        PropsToken(),
    )

    override fun builder(): Builder {
        return DoorModelBuilder()
    }
}
