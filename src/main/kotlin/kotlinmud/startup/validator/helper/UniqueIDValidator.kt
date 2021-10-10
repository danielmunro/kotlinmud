package kotlinmud.startup.validator.helper

import kotlinmud.startup.exception.DuplicateIdValidationException
import kotlinmud.startup.model.Model

fun validateUniqueIds(models: List<Model>) {
    val idSet = mutableSetOf<Int>()
    models.forEach {
        if (idSet.contains(it.id)) {
            throw DuplicateIdValidationException(it.id)
        }
        idSet.add(it.id)
    }
}
