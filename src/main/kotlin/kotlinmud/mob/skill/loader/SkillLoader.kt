package kotlinmud.mob.skill.loader

import kotlinmud.fs.loader.Tokenizer
import kotlinmud.helper.LOOP_END_DELIMITER
import kotlinmud.mob.skill.type.SkillType

class SkillLoader(private val tokenizer: Tokenizer) {
    fun load(): Map<SkillType, Int> {
        val skills = mutableMapOf<SkillType, Int>()
        while (tokenizer.peek() != LOOP_END_DELIMITER) {
            val line = tokenizer.parseString().split(" ")
            skills[SkillType.valueOf(line[0])] = line[1].toInt()
        }
        tokenizer.parseString()
        return skills
    }
}
