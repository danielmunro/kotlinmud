package kotlinmud.mob.skill.factory

import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.dao.SkillDAO
import kotlinmud.mob.skill.type.SkillType
import org.jetbrains.exposed.sql.transactions.transaction

fun createSkill(skillType: SkillType, mob: Mob, level: Int = 1): SkillDAO {
    val skill = transaction {
        SkillDAO.new {
            type = skillType
            this.level = level
        }
    }
    mob.skills.add(skill)
    return skill
}
