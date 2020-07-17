package kotlinmud.mob.skill.factory

import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.skill.dao.SkillDAO
import kotlinmud.mob.skill.type.SkillType
import org.jetbrains.exposed.sql.transactions.transaction

fun createSkill(skillType: SkillType, mob: MobDAO, level: Int = 1): SkillDAO {
    return transaction {
        SkillDAO.new {
            type = skillType
            this.mob = mob
            this.level = level
        }
    }
}
