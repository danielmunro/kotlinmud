package kotlinmud.io.model

import com.thinkinglogic.builder.annotation.Builder
import com.thinkinglogic.builder.annotation.DefaultValue

@Builder
data class Message(
    @DefaultValue("\"\"") val toActionCreator: String,
    @DefaultValue("\"\"") val toTarget: String,
    @DefaultValue("\"\"") val toObservers: String,
    @DefaultValue("true") val sendPrompt: Boolean
)
