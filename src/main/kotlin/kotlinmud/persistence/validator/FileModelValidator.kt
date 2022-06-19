package kotlinmud.persistence.validator

import kotlinmud.persistence.model.FileModel
import kotlinmud.persistence.validator.helper.validateUniqueIds

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
}
