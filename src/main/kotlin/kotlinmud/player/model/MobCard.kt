package kotlinmud.player.model

import com.thinkinglogic.builder.annotation.Builder
import com.thinkinglogic.builder.annotation.DefaultValue
import com.thinkinglogic.builder.annotation.Mutable
import kotlinmud.attributes.Attributes
import kotlinmud.mob.Appetite

@Builder
class MobCard(
    val mobName: String,
    val playerEmail: String,
    val experiencePerLevel: Int,
    @DefaultValue("0") var experience: Int,
    @DefaultValue("0") var trains: Int,
    @DefaultValue("0") var practices: Int,
    @DefaultValue("0") var bounty: Int,
    @DefaultValue("0") var sacPoints: Int,
    var appetite: Appetite,
    @DefaultValue("Attributes()") @Mutable val attributes: Attributes,
    @DefaultValue("0") var skillPoints: Int
)
