package kotlinmud.startup.spec

import kotlinmud.startup.model.builder.Builder
import kotlinmud.startup.model.builder.MobModelBuilder
import kotlinmud.startup.token.BriefToken
import kotlinmud.startup.token.DescriptionToken
import kotlinmud.startup.token.IdToken
import kotlinmud.startup.token.NameToken
import kotlinmud.startup.token.PropsToken

class MobSpec : Spec {
    override val tokens = listOf(
        IdToken(),
        NameToken(),
        BriefToken(),
        DescriptionToken(),
        PropsToken(),
    )

    override fun builder(): Builder {
        return MobModelBuilder()
    }
}
