package kotlinmud.player.auth.service

import kotlinmud.mob.skill.helper.createCreationGroupList
import kotlinmud.mob.skill.type.CreationGroup

class CustomizationService(private val mobName: String) {
    private val customizations = createCreationGroupList().toMutableList()
    private val added = mutableListOf<CreationGroup>()

    fun getPoints(): Int {
        return added.fold(0) { acc, it -> acc + it.points }
    }
}
