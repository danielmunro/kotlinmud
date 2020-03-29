package kotlinmud.mob

import kotlinmud.mob.skill.Cost

interface HasCosts {
    val costs: List<Cost>
}
