package kotlinmud.startup.validator

import kotlinmud.startup.model.FileModel
import kotlinmud.startup.validator.helper.validateUniqueIds

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
