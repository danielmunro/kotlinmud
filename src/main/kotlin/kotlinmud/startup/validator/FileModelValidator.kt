package kotlinmud.startup.validator

import kotlinmud.startup.exception.DuplicateIdValidationException
import kotlinmud.startup.model.FileModel
import kotlinmud.startup.model.Model

class FileModelValidator(private val file: FileModel) : Validator {
    override fun validate() {
        validateRooms()
        validateMobs()
        validateItems()
    }

    private fun validateRooms() {
        validateUniqueIds(file.rooms)
    }

    private fun validateMobs() {
        validateUniqueIds(file.mobs)
    }

    private fun validateItems() {
        validateUniqueIds(file.items)
    }

    private fun validateUniqueIds(models: List<Model>) {
        val idSet = mutableSetOf<Int>()
        models.forEach {
            if (idSet.contains(it.id)) {
                throw DuplicateIdValidationException(it.id)
            }
            idSet.add(it.id)
        }
    }
}
