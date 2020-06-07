package kotlinmud.mob.type

import kotlinmud.mob.skill.model.Cost

interface HasCosts {
    val costs: List<Cost>
}
