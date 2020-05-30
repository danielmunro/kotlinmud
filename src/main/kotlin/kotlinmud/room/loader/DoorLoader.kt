package kotlinmud.room.loader

import kotlinmud.fs.loader.Tokenizer
import kotlinmud.fs.loader.area.loader.Loader
import kotlinmud.fs.loader.area.model.DoorModel
import kotlinmud.room.type.getDoorDispositionFromString

class DoorLoader(private val tokenizer: Tokenizer) : Loader {
    override fun load(): DoorModel {
        val id = tokenizer.parseInt()
        val name = tokenizer.parseString()
        val description = tokenizer.parseString()
        val disposition = tokenizer.parseString()

        return DoorModel(id, name, description,
            getDoorDispositionFromString(disposition)
        )
    }
}
