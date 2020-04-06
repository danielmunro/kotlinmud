package kotlinmud.item

import com.thinkinglogic.builder.annotation.Builder
import com.thinkinglogic.builder.annotation.DefaultValue
import com.thinkinglogic.builder.annotation.Mutable

@Builder
class Inventory(
    @DefaultValue("mutableListOf()") @Mutable val items: MutableList<Item> = mutableListOf()
)
