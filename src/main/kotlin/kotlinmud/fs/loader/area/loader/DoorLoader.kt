package kotlinmud.fs.loader.area.loader

import kotlinmud.fs.loader.Tokenizer
import kotlinmud.fs.loader.area.model.DoorModel
import kotlinmud.world.room.exit.getDoorDispositionFromString

class DoorLoader(private val tokenizer: Tokenizer) : Loader {
    var id = 0
    var name = ""
    var description = ""
    var disposition = ""

    override fun load(): DoorModel {
        id = tokenizer.parseInt()
        name = tokenizer.parseString()
        description = tokenizer.parseString()
        disposition = tokenizer.parseString()

        return DoorModel(id, name, description, getDoorDispositionFromString(disposition))
    }
}
