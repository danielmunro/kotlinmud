package kotlinmud.persistence.dumper

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.helper.createTestService
import org.junit.Test

class MobDumperServiceTest {
    @Test
    fun testMobDumperServiceCanDumpAMob() {
        // setup
        val test = createTestService(false)

        // given
        val startupService = test.createStartupService(
            listOf(
                """
area:
2. Test

rooms:
1. a test room
a test room~
~

mobs:
1. a red fox
a small red fox is darting through the trees
~
job quest~
race canid~
~
1 1 1~
~
"""
            )
        )
        startupService.hydrateWorld()
        val mobDumperService = test.getMobDumperService()

        // when
        val dump = mobDumperService.dump()

        // then
        assertThat(dump).isEqualTo(
            """mobs:
1. a red fox
a small red fox is darting through the trees
~
job quest~
race canid~
~
1 1 1~
~"""
        )
    }
}
