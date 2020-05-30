package kotlinmud.player.model

import com.thinkinglogic.builder.annotation.Builder
import com.thinkinglogic.builder.annotation.DefaultValue
import com.thinkinglogic.builder.annotation.Mutable
import kotlinmud.attributes.model.Attributes
import kotlinmud.attributes.type.Attribute
import kotlinmud.mob.model.AddExperience
import kotlinmud.mob.model.Appetite

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
    @DefaultValue("mutableListOf()") @Mutable val trainedAttributes: MutableList<Attributes>,
    @DefaultValue("0") var skillPoints: Int
) {
    fun calcTrained(attribute: Attribute): Int {
        return trainedAttributes.fold(0) { acc, it -> acc + it.getAttribute(attribute) }
    }

    fun addExperience(level: Int, value: Int): AddExperience {
        val toLevelInitial = getExperienceToLevel(level)
        if (toLevelInitial < 0) {
            return AddExperience(0, true)
        }
        experience += value
        var didLevel = false
        val toLevel = getExperienceToLevel(level)
        if (toLevel < 0) {
            didLevel = true
        }
        return AddExperience(experience, didLevel)
    }

    private fun getExperienceToLevel(level: Int): Int {
        return experiencePerLevel - experience + (experiencePerLevel * level)
    }
}
