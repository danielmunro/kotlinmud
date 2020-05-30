package kotlinmud.action.impl.social

import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.attributes.type.Attribute
import kotlinmud.io.MessageBuilder
import kotlinmud.io.createResponseWithEmptyActionContext

fun createAttributesAction(): Action {
    return Action(Command.ATTRIBUTES) {
        val mob = it.getMob()
        val message =
            "Your attributes are:\nStr: ${mob.base(Attribute.STR)}/${mob.calc(
                Attribute.STR)} Int: ${mob.base(Attribute.INT)}/${mob.calc(
                Attribute.INT
            )} Wis: ${mob.base(Attribute.WIS)}/${mob.calc(
                Attribute.WIS)} Dex: ${mob.base(Attribute.DEX)}/${mob.calc(
                Attribute.DEX
            )} Con: ${mob.base(Attribute.CON)}/${mob.calc(
                Attribute.CON)}"
        createResponseWithEmptyActionContext(
            MessageBuilder()
                .toActionCreator(message)
                .toObservers(message)
                .build()
        )
    }
}
