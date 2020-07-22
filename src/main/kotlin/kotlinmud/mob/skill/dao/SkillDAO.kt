package kotlinmud.mob.skill.dao

import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.skill.table.Skills
import kotlinmud.mob.skill.type.SkillType
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class SkillDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SkillDAO>(Skills)

    var type by Skills.type.transform(
        { it.toString() },
        { SkillType.valueOf(it) }
    )
    var level by Skills.level
    var mob by MobDAO referencedOn Skills.mobId
}
