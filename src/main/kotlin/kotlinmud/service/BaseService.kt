package kotlinmud.service

import kotlinmud.persistence.model.Model

open class BaseService {
    protected val models = mutableListOf<Model>()
    private var autoId = 1

    fun getNextAutoId(): Int {
        val toReturn = autoId
        autoId++
        return toReturn
    }

    fun addModel(model: Model) {
        if (model.id >= autoId) {
            autoId = model.id + 1
        }
        models.add(model)
    }
}
