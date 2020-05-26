package kotlinmud.player.mapper

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
str: ${mobCard.attributes.strength}, int: ${mobCard.attributes.intelligence}, wis: ${mobCard.attributes.wisdom}, dex: ${mobCard.attributes.dexterity}, con: ${mobCard.attributes.constitution}, hp: ${mobCard.attributes.hp}, mana: ${mobCard.attributes.mana}, mv: ${mobCard.attributes.mv}, hit: ${mobCard.attributes.hit}, dam: ${mobCard.attributes.dam}, acSlash: ${mobCard.attributes.acSlash}, acBash: ${mobCard.attributes.acBash}, acPierce: ${mobCard.attributes.acPierce}, acMagic: ${mobCard.attributes.acMagic}~
"""
}
