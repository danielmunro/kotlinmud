package kotlinmud.action.impl.social

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.attributes.Attribute
import kotlinmud.io.Message
import kotlinmud.io.createResponseWithEmptyActionContext

fun createAttributesAction(): Action {
    return Action(Command.ATTRIBUTES) {
        val mob = it.getMob()
        createResponseWithEmptyActionContext(
            Message(
                "Your attributes are:\nStr: ${mob.base(Attribute.STR)}/${mob.calc(Attribute.STR)} Int: ${mob.base(Attribute.INT)}/${mob.calc(Attribute.INT)} Wis: ${mob.base(Attribute.WIS)}/${mob.calc(Attribute.WIS)} Dex: ${mob.base(Attribute.DEX)}/${mob.calc(Attribute.DEX)} Con: ${mob.base(Attribute.CON)}/${mob.calc(Attribute.CON)}"
            )
        )
    }
}
