package kotlinmud.loader.loader

import kotlinmud.loader.Tokenizer
import kotlinmud.loader.model.DoorModel
import kotlinmud.room.exit.getDoorDispositionFromString

class DoorLoader(private val tokenizer: Tokenizer): Loader {
    var id = 0
    var name = ""
    var description = ""
    var disposition = ""

    override fun load(): DoorModel {
        id = tokenizer.parseId()
        name = tokenizer.parseString()
        description = tokenizer.parseString()
        disposition = tokenizer.parseString()

        return DoorModel(id, name, description, getDoorDispositionFromString(disposition))
    }
}
