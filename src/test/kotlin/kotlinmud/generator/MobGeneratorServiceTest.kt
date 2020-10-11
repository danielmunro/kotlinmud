package kotlinmud.generator

import assertk.assertThat
import assertk.assertions.isGreaterThan
import kotlinmud.biome.helper.createBiomes
import kotlinmud.generator.service.MobGeneratorService
import kotlinmud.mob.table.Mobs
import kotlinmud.test.createTestServiceWithResetDB
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class MobGeneratorServiceTest {
    @Test
    fun canGenerateMobs() {
        // setup
        createTestServiceWithResetDB()
        val biomes = createBiomes()
        val initialCount = transaction { Mobs.selectAll().count() }

        // given
        val generatorService = MobGeneratorService(biomes)

        // when
        generatorService.respawnMobs()

        // then
        assertThat(transaction { Mobs.selectAll().count() }).isGreaterThan(initialCount)
    }
}
