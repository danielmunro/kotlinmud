package kotlinmud.persistence.spec

import kotlinmud.persistence.model.builder.Builder
import kotlinmud.persistence.model.builder.RoomModelBuilder
import kotlinmud.persistence.token.DescriptionToken
import kotlinmud.persistence.token.IdToken
import kotlinmud.persistence.token.NameToken
import kotlinmud.persistence.token.PropsToken

class RoomSpec : Spec {
    override val tokens = listOf(
        IdToken(),
        NameToken(),
        DescriptionToken(),
        PropsToken(),
    )

    override fun builder(): Builder {
        return RoomModelBuilder()
    }
}
