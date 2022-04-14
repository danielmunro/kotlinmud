package kotlinmud.startup.spec

import kotlinmud.room.type.Area
import kotlinmud.startup.model.builder.Builder
import kotlinmud.startup.model.builder.ItemModelBuilder
import kotlinmud.startup.token.BriefToken
import kotlinmud.startup.token.DescriptionToken
import kotlinmud.startup.token.IdToken
import kotlinmud.startup.token.ItemRespawnToken
import kotlinmud.startup.token.NameToken
import kotlinmud.startup.token.PropsToken

class ItemSpec(private val area: Area) : Spec {
    override val tokens = listOf(
        IdToken(),
        NameToken(),
        BriefToken(),
        DescriptionToken(),
        PropsToken(),
        ItemRespawnToken(),
    )

    override fun builder(): Builder {
        return ItemModelBuilder().also {
            it.area = area
        }
    }
}
