package kotlinmud.action.impl.info

import kotlinmud.action.helper.mustBeAlive
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.attributes.type.Attribute
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.model.createResponseWithEmptyActionContext
import kotlinmud.mob.type.CurrencyType

fun createScoreAction(): Action {
    return Action(Command.SCORE, mustBeAlive()) { svc ->
        val mob = svc.getMob()
        val items = mob.items
        createResponseWithEmptyActionContext(
            messageToActionCreator(
                "You are $mob, unknown years old.\n" +
                    "hp, mana, mv status lines.\n" +
                    "You have ${mob.hp} of ${mob.calc(Attribute.HP)} hit points," +
                    "${mob.mana} of ${mob.calc(Attribute.MANA)} mana, " +
                    "${mob.mv} of ${mob.calc(Attribute.MV)} moves.\n" +
                    "Race: ${mob.race.type.toString().lowercase()} Gender: ${
                    mob.gender.toString().lowercase()
                    } " +
                    "Class: ${mob.specialization.toString().lowercase()} Kit: none\n" +
                    "Trains: ${mob.trains}  Practices: ${mob.practices}  Skill Points: ${mob.skillPoints}  Bounty: ${mob.bounty} " +
                    "You are carrying ${items.size}/${mob.maxItems} items, ${
                    items.map { it.weight }
                        .fold(0.0) { acc: Double, value: Double -> acc + value }
                    }/${mob.maxWeight} weight capacity.\n" +
                    "Str: ${mob.base(Attribute.STR)}/${
                    mob.calc(
                        Attribute.STR
                    )
                    } " +
                    "Int: ${mob.base(Attribute.INT)}/${
                    mob.calc(
                        Attribute.INT
                    )
                    } " +
                    "Wis: ${mob.base(Attribute.WIS)}/${
                    mob.calc(
                        Attribute.WIS
                    )
                    } " +
                    "Dex: ${mob.base(Attribute.DEX)}/${
                    mob.calc(
                        Attribute.DEX
                    )
                    } " +
                    "Con: ${mob.base(Attribute.CON)}/${
                    mob.calc(
                        Attribute.CON
                    )
                    }\n" +
                    "You have ${mob.experience} exp, ${mob.getCurrency(CurrencyType.Gold)} gold, ${mob.getCurrency(CurrencyType.Silver)} silver, ${mob.getCurrency(CurrencyType.Copper)} copper.\n" +
                    "You need ${mob.getRemainingExperience()} exp to level.\n" +
                    "Wimpy set to ${mob.wimpy}.\n" +
                    "You are ${mob.disposition.toString().lowercase()}.\n" +
                    "Saving Throw Adjustment: ${mob.savingThrows}\n" +
                    "You are hopelessly vulnerable to piercing.\n" +
                    "You are hopelessly vulnerable to bashing.\n" +
                    "You are hopelessly vulnerable to slashing.\n" +
                    "You are hopelessly vulnerable to magic.\n" +
                    "You are a neutral follower of yourself.\n" +
                    "Sac Points: ${mob.sacPoints}"
            )
        )
    }
}
