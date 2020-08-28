package kotlinmud.player.auth.service

import kotlinmud.helper.string.matches
import kotlinmud.mob.skill.helper.createCreationGroupList
import kotlinmud.mob.skill.type.CreationGroup

class CustomizationService(private val mobName: String) {
    private val customizations = createCreationGroupList().toMutableList()
    private val added = mutableListOf<CreationGroup>()

    fun getPoints(): Int {
        return added.fold(0) { acc, it -> acc + it.points }
    }

    fun findCreationGroup(name: String): CreationGroup? {
        return customizations.find { name.matches(it.name) } ?: added.find { name.matches(it.name) }
    }

    fun add(creationGroup: CreationGroup) {
        customizations.remove(creationGroup)
        added.add(creationGroup)
    }

    fun remove(creationGroup: CreationGroup) {
        added.remove(creationGroup)
        customizations.add(creationGroup)
    }
}
