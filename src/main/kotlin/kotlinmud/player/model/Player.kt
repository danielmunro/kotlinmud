package kotlinmud.player.model

import com.thinkinglogic.builder.annotation.Builder
import com.thinkinglogic.builder.annotation.DefaultValue
import com.thinkinglogic.builder.annotation.Mutable
import org.joda.time.LocalDate

@Builder
data class Player(
    val email: String,
    @DefaultValue("\"\"") var name: String,
    @DefaultValue("LocalDate.now()") val created: LocalDate,
    @DefaultValue("mutableListOf()") @Mutable val mobs: MutableList<String>
) {
    var lastOTP: String = ""
}
