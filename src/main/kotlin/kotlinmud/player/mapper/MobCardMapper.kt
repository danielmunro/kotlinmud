package kotlinmud.player.mapper

import kotlinmud.player.model.MobCard

fun mapMobCard(mobCard: MobCard): String {
    return """${mobCard.playerEmail}~
${mobCard.mobName}~
#${mobCard.experience}
#${mobCard.experiencePerLevel}
#${mobCard.sacPoints}
#${mobCard.trains}
#${mobCard.practices}
#${mobCard.appetite.maxHunger}
#${mobCard.appetite.maxThirst}
#${mobCard.appetite.getHunger()}
#${mobCard.appetite.getThirst()}
#${mobCard.bounty}
str: ${mobCard.attributes.strength}, int: ${mobCard.attributes.intelligence}, wis: ${mobCard.attributes.wisdom}, dex: ${mobCard.attributes.dexterity}, con: ${mobCard.attributes.constitution}, hp: ${mobCard.attributes.hp}, mana: ${mobCard.attributes.mana}, mv: ${mobCard.attributes.mv}, hit: ${mobCard.attributes.hit}, dam: ${mobCard.attributes.dam}, acSlash: ${mobCard.attributes.acSlash}, acBash: ${mobCard.attributes.acBash}, acPierce: ${mobCard.attributes.acPierce}, acMagic: ${mobCard.attributes.acMagic}~
"""
}
