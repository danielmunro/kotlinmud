package kotlinmud.loader

class Tokenizer(val value: String) {
    private val raw = value.split(" ", "\n")
    private var i = 0

    private fun getNext(): String {
        if (i == raw.size) {
            throw Exception("EOF")
        }
        val value = raw[i]
        i++
        return value.trim()
    }

    fun parseId(): Int {
        val next = getNext()
        if (next == "") {
            return parseId()
        }
        return next.substring(1).toInt()
    }

    fun parseString(): String {
        var buf = getNext()
        var toReturn = ""
        while (!buf.contains("~")) {
            toReturn += if (buf == "") "\n\n" else "$buf "
            buf = getNext()
        }
        toReturn += buf.substring(0, buf.length - 1)
        return toReturn
    }

    fun parseProperties(): Map<String, String> {
        var key = getNext()
        if (key == "~") {
            return mapOf()
        }
        var value = getNext()
        val map = mutableMapOf<String, String>()
        while (!value.contains("~")) {
            map += Pair(key.trim(':'), value.trim(',', '~'))
            key = getNext()
            value = getNext()
        }
        map += Pair(key.trim(':'), value.trim(',', '~'))
        return map
    }
}
