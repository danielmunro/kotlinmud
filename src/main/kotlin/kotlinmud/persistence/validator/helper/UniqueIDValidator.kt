package kotlinmud.persistence.validator.helper

import kotlinmud.persistence.exception.DuplicateIdValidationException
import kotlinmud.persistence.model.Model

fun validateUniqueIds(models: List<Model>) {
    val idSet = mutableSetOf<Int>()
    models.forEach {
        if (idSet.contains(it.id)) {
            throw DuplicateIdValidationException(it.id)
        }
        idSet.add(it.id)
    }
}
