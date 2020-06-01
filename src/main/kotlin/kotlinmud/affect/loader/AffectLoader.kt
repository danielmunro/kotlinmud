package kotlinmud.affect.loader

import kotlinmud.affect.model.AffectInstance
import kotlinmud.affect.type.AffectType
import kotlinmud.fs.loader.Tokenizer
import kotlinmud.helper.LOOP_END_DELIMITER

class AffectLoader(private val tokenizer: Tokenizer) {
    fun load(): List<AffectInstance> {
        val affects = mutableListOf<AffectInstance>()
        while (tokenizer.peek() != LOOP_END_DELIMITER) {
            val line = tokenizer.parseString().split(" ")
            affects.add(AffectInstance(AffectType.valueOf(line[0]), line[1].toInt()))
        }
        tokenizer.parseString()
        return affects
    }
}
