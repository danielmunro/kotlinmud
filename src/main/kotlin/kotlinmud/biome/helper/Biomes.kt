package kotlinmud.biome.helper

import kotlinmud.biome.impl.Arboreal
import kotlinmud.biome.impl.Badlands
import kotlinmud.biome.impl.Desert
import kotlinmud.biome.impl.Jungle
import kotlinmud.biome.impl.Mountain
import kotlinmud.biome.impl.Plains
import kotlinmud.biome.impl.Tundra
import kotlinmud.biome.type.Biome
import org.jetbrains.exposed.sql.transactions.transaction

fun createBiomes(): List<Biome> {
    return transaction {
        listOf(
            Plains(),
            Arboreal(),
            Tundra(),
            Desert(),
            Jungle(),
            Mountain(),
            Badlands()
        )
    }
}
