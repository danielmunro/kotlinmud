package kotlinmud.startup.spec

import kotlinmud.startup.model.builder.Builder
import kotlinmud.startup.model.builder.RoomModelBuilder
import kotlinmud.startup.token.DescriptionToken
import kotlinmud.startup.token.IdToken
import kotlinmud.startup.token.NameToken
import kotlinmud.startup.token.PropsToken

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
