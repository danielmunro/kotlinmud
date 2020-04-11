package kotlinmud.fs.loader.loader

import kotlinmud.fs.loader.Tokenizer
import kotlinmud.fs.loader.model.DoorModel
import kotlinmud.world.room.exit.getDoorDispositionFromString

class DoorLoader(private val tokenizer: Tokenizer) : Loader {
    var id = 0
    var name = ""
    var description = ""
    var disposition = ""
    override var props: Map<String, String>
        get() = TODO("Not yet implemented")
        set(_) {}

    override fun load(): DoorModel {
        id = tokenizer.parseInt()
        name = tokenizer.parseString()
        description = tokenizer.parseString()
        disposition = tokenizer.parseString()

        return DoorModel(id, name, description, getDoorDispositionFromString(disposition))
    }
}