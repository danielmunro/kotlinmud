package kotlinmud.action.impl.admin

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.mob.type.Role
import kotlinmud.mob.type.Standing
import kotlinmud.test.helper.createTestService
import kotlinmud.test.helper.getIdentifyingWord
import org.junit.Test

class BanTest {
    @Test
    fun testBanSanityCheck() {
        // setup
        val test = createTestService()

        // given
        val admin = test.createPlayerMobBuilder().also {
            it.role = Role.Admin
        }.build()
        val mob = test.createPlayerMob()

        // when
        val response = test.runAction(admin, "ban ${getIdentifyingWord(mob)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("$mob is now banned.")
    }

    @Test
    fun testCannotRebanUser() {
        // setup
        val test = createTestService()

        // given
        val admin = test.createPlayerMobBuilder().also {
            it.role = Role.Admin
        }.build()
        val mob = test.createPlayerMob()
        mob.standing = Standing.Banned

        // when
        val response = test.runAction(admin, "ban ${getIdentifyingWord(mob)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("They are already banned.")
    }
}
