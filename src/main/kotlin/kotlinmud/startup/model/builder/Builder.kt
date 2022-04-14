package kotlinmud.startup.model.builder

import kotlinmud.startup.model.Model

interface Builder {
    var id: Int
    var name: String
    var brief: String
    var description: String
    var keywords: Map<String, String>
    var respawns: List<RespawnSpec>
    fun build(): Model
}
