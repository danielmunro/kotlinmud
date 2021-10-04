package kotlinmud.startup.spec

import kotlinmud.startup.model.builder.Builder
import kotlinmud.startup.model.builder.RoomModelBuilder
import kotlinmud.startup.parser.Token

class RoomSpec : Spec {
    override val tokens = listOf(
        Token.ID,
        Token.Name,
        Token.Description,
        Token.Props,
    )

    override fun builder(): Builder {
        return RoomModelBuilder()
    }
}
