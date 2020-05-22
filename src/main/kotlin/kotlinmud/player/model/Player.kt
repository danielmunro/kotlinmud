package kotlinmud.player.model

import com.thinkinglogic.builder.annotation.Builder
import com.thinkinglogic.builder.annotation.DefaultValue
import com.thinkinglogic.builder.annotation.Mutable
import org.joda.time.LocalDateTime

@Builder
data class Player(
    val email: String,
    @DefaultValue("\"\"") var name: String,
    @DefaultValue("LocalDateTime.now()") val created: LocalDateTime,
    @DefaultValue("mutableListOf()") @Mutable val mobs: MutableList<String>
) {
    var lastOTP: String = ""
}
