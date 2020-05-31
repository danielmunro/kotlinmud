package kotlinmud.player.mapper

import kotlinmud.attributes.mapper.mapAttributes
import kotlinmud.fs.int
import kotlinmud.fs.str
import kotlinmud.player.model.MobCard

fun mapMobCard(mobCard: MobCard): String {
    return """${str(mobCard.playerEmail)}
${str(mobCard.mobName)}
${int(mobCard.experience)}
${int(mobCard.experiencePerLevel)}
${int(mobCard.sacPoints)}
${int(mobCard.trains)}
${int(mobCard.practices)}
${int(mobCard.appetite.maxHunger)}
${int(mobCard.appetite.maxThirst)}
${int(mobCard.appetite.getHunger())}
${int(mobCard.appetite.getThirst())}
${int(mobCard.bounty)}
${int(mobCard.skillPoints)}
${mobCard.trainedAttributes.joinToString("\n") { mapAttributes(it) }}
end
"""
}
