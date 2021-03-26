package kotlinmud.io.service

import kotlinmud.mob.model.PlayerMob
import kotlinmud.mob.type.Disposition
import kotlinmud.room.model.Room

class RequestService(val mob: PlayerMob, val input: String) {
    val args: List<String>
    init {
        val setupArgs: MutableList<String> = mutableListOf()
        var buffer = ""
        var isOpen = false
        input.toLowerCase().forEach {
            if (it == '\'') {
                if (isOpen) {
                    setupArgs.add(buffer)
                    buffer = ""
                    isOpen = false
                } else {
                    buffer = ""
                    isOpen = true
                }
            } else if (it == ' ' && !isOpen && buffer != "") {
                setupArgs.add(buffer)
                buffer = ""
            } else if (isOpen || it != ' ') {
                buffer += it
            }
        }
        if (buffer != "") {
            setupArgs.add(buffer)
        }
        args = setupArgs
    }

    fun getRoom(): Room {
        return mob.room
    }

    fun getCommand(): String {
        return args[0]
    }

    fun getSubject(): String {
        return if (args.size > 1) args[1] else ""
    }

    fun getDisposition(): Disposition {
        return mob.disposition
    }
}
