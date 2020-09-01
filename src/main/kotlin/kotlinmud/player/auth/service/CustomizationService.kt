package kotlinmud.player.auth.service

import kotlinmud.helper.string.matches
import kotlinmud.mob.skill.helper.createCreationGroupList
import kotlinmud.mob.skill.type.Customization

class CustomizationService(private val mobName: String) {
    private val customizations = createCreationGroupList().toMutableList()
    private val added = mutableListOf<Customization>()

    fun getPoints(): Int {
        return added.fold(0) { acc, it -> acc + it.points }
    }

    fun findAddedCustomization(name: String): Customization? {
        return added.find { name.matches(it.name) }
    }

    fun findCustomizationFromPool(name: String): Customization? {
        return customizations.find { name.matches(it.name) }
    }

    fun getUnlearned(): List<Customization> {
        return customizations
    }

    fun getLearned(): List<Customization> {
        return added
    }

    fun add(customization: Customization) {
        customizations.remove(customization)
        added.add(customization)
    }

    fun remove(customization: Customization) {
        added.remove(customization)
        customizations.add(customization)
    }
}
