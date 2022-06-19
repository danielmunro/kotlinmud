package kotlinmud.persistence.validator

import assertk.fail
import kotlinmud.attributes.type.Attribute
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Position
import kotlinmud.item.type.Weapon
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.persistence.exception.ItemValidatorException
import kotlinmud.test.helper.createTestService
import org.junit.Test

class ItemValidatorTest {
    @Test
    fun testItemValidatorFailsWithoutPosition() {
        // setup
        val test = createTestService()

        // when
        val item = test.createItemBuilder()
            .also {
                it.type = ItemType.EQUIPMENT
            }
            .build()

        // then
        try {
            ItemValidator(item).validate()
            fail("expected item validator exception")
        } catch (e: ItemValidatorException) {
        }
    }

    @Test
    fun testItemValidatorFailsWeaponWithoutWeaponType() {
        // setup
        val test = createTestService()

        // when
        val item = test.createItemBuilder()
            .also {
                it.type = ItemType.EQUIPMENT
                it.position = Position.WEAPON
                it.damageType = DamageType.SLASH
                it.attackVerb = "slice"
                it.attributes[Attribute.HIT] = 1
                it.attributes[Attribute.DAM] = 1
            }
            .build()

        // then
        try {
            ItemValidator(item).validate()
            fail("expected item validator exception")
        } catch (e: ItemValidatorException) {
        }
    }

    @Test
    fun testItemValidatorFailsWeaponWithoutDamageType() {
        // setup
        val test = createTestService()

        // when
        val item = test.createItemBuilder()
            .also {
                it.type = ItemType.EQUIPMENT
                it.position = Position.WEAPON
                it.weaponType = Weapon.AXE
                it.attackVerb = "slice"
                it.attributes[Attribute.HIT] = 1
                it.attributes[Attribute.DAM] = 1
            }
            .build()

        // then
        try {
            ItemValidator(item).validate()
            fail("expected item validator exception")
        } catch (e: ItemValidatorException) {
        }
    }

    @Test
    fun testItemValidatorFailsWeaponWithoutAttackVerb() {
        // setup
        val test = createTestService()

        // when
        val item = test.createItemBuilder()
            .also {
                it.type = ItemType.EQUIPMENT
                it.position = Position.WEAPON
                it.weaponType = Weapon.AXE
                it.damageType = DamageType.SLASH
                it.attributes[Attribute.HIT] = 1
                it.attributes[Attribute.DAM] = 1
            }
            .build()

        // then
        try {
            ItemValidator(item).validate()
            fail("expected item validator exception")
        } catch (e: ItemValidatorException) {
        }
    }

    @Test
    fun testItemValidatorFailsWeaponWithoutHit() {
        // setup
        val test = createTestService()

        // when
        val item = test.createItemBuilder()
            .also {
                it.type = ItemType.EQUIPMENT
                it.position = Position.WEAPON
                it.weaponType = Weapon.AXE
                it.damageType = DamageType.SLASH
                it.attackVerb = "slice"
                it.attributes[Attribute.DAM] = 1
            }
            .build()

        // then
        try {
            ItemValidator(item).validate()
            fail("expected item validator exception")
        } catch (e: ItemValidatorException) {
        }
    }

    @Test
    fun testItemValidatorFailsWeaponWithoutDam() {
        // setup
        val test = createTestService()

        // when
        val item = test.createItemBuilder()
            .also {
                it.type = ItemType.EQUIPMENT
                it.position = Position.WEAPON
                it.weaponType = Weapon.AXE
                it.damageType = DamageType.SLASH
                it.attackVerb = "slice"
                it.attributes[Attribute.HIT] = 1
            }
            .build()

        // then
        try {
            ItemValidator(item).validate()
            fail("expected item validator exception")
        } catch (e: ItemValidatorException) {
        }
    }
}
