package kotlinmud.action.impl

import kotlinmud.action.*
import kotlinmud.attributes.Attribute
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.io.createResponseWithEmptyActionContext

fun createScoreAction(): Action {
    return Action(
        Command.SCORE,
        mustBeAlive(),
        listOf(Syntax.COMMAND),
        { _: ActionContextService, request: Request ->
            val mob = request.mob
            createResponseWithEmptyActionContext(
                Message(
                    "You are $mob, unknown years old.\n" +
                            "hp, mana, mv status lines.\n" +
                            "You have ${mob.hp} of ${mob.calc(Attribute.HP)} hit points," +
                            "${mob.mana} of ${mob.calc(Attribute.MANA)} mana, " +
                            "${mob.mv} of ${mob.calc(Attribute.MV)} moves.\n" +
                            "Race: ${mob.race.type.toString().toLowerCase()} Gender: ${mob.gender.toString().toLowerCase()} " +
                            "Class: ${mob.specialization.toString().toLowerCase()} Kit: none\n" +
                            "Trains: ${mob.trains}  Practices: ${mob.practices}  Skill Points: ${mob.skillPoints}  Bounty: ${mob.bounty} " +
                            "You are carrying ${mob.inventory.items.size}/${mob.inventory.maxItems} items, ${mob.inventory.items.map{ it.weight }.fold(0.0) { acc: Double, value: Double -> acc + value}}/${mob.inventory.maxWeight} weight capacity.\n" +
                            "Str: ${mob.base(Attribute.STR)}/${mob.calc(Attribute.STR)} " +
                            "Int: ${mob.base(Attribute.INT)}/${mob.calc(Attribute.INT)} " +
                            "Wis: ${mob.base(Attribute.WIS)}/${mob.calc(Attribute.WIS)} " +
                            "Dex: ${mob.base(Attribute.DEX)}/${mob.calc(Attribute.DEX)} " +
                            "Con: ${mob.base(Attribute.CON)}/${mob.calc(Attribute.CON)}\n" +
                            "You have ${mob.experience} exp, ${mob.gold} gold, 0 silver.\n" +
                            "You need 0 exp to level.\n" +
                            "Wimpy set to ${mob.wimpy}.\n" +
                            "You are ${mob.disposition.toString().toLowerCase()}.\n" +
                            "Saving Throw Adjustment: ${mob.getSaves()}\n" +
                            "You are hopelessly vulnerable to piercing.\n" +
                            "You are hopelessly vulnerable to bashing.\n" +
                            "You are hopelessly vulnerable to slashing.\n" +
                            "You are hopelessly vulnerable to magic.\n" +
                            "You are a neutral follower of yourself.\n" +
                            "Sac Points: ${mob.sacPoints}"
                )
            )
        }
    )
}
