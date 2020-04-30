package kotlinmud.data

class Table<T : Row>(private val rows: MutableList<T>) {
    private val rowsById: MutableMap<Int, T> = mutableMapOf()

    init {
        rows.forEach {
            rowsById[it.id] = it
        }
    }

    fun add(row: T) {
        rows.add(row)
        rowsById[row.id] = row
    }

    fun get(id: Int): T {
        return rowsById[id]!!
    }

    fun toList(): List<T> {
        return rows
    }
}
