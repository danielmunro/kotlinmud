package kotlinmud.data

class Table<T : Row>(private val rows: List<T>) {
    private val rowsById: MutableMap<Int, T> = mutableMapOf()

    init {
        rows.forEach {
            rowsById[it.id] = it
        }
    }

    fun get(id: Int): T {
        return rowsById[id]!!
    }

    fun toList(): List<T> {
        return rows
    }
}
