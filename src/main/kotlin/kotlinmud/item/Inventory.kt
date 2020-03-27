package kotlinmud.item

import com.thinkinglogic.builder.annotation.Builder
import com.thinkinglogic.builder.annotation.DefaultValue
import com.thinkinglogic.builder.annotation.Mutable

@Builder
class Inventory(
    @DefaultValue("mutableListOf()") @Mutable val items: MutableList<Item> = mutableListOf(),
    @DefaultValue("50") val maxItems: Int = 0,
    @DefaultValue("100") val maxWeight: Int = 0
) {
    fun getItemGroups(): Map<Int, List<Item>> {
        return items.groupBy { it.id }
    }
}
