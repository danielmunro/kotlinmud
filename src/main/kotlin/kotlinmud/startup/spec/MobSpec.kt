package kotlinmud.startup.spec

import kotlinmud.startup.model.builder.Builder
import kotlinmud.startup.model.builder.MobModelBuilder
import kotlinmud.startup.parser.Token

class MobSpec : Spec {
    override val tokens = listOf(
        Token.ID,
        Token.Name,
        Token.Brief,
        Token.Description,
        Token.Props,
    )

    override fun builder(): Builder {
        return MobModelBuilder()
    }
}
