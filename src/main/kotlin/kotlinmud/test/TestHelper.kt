package kotlinmud.test

import kotlinmud.db.applyDBSchema
import kotlinmud.db.connect
import kotlinmud.db.disconnect

fun globalSetup() {
    connect()
    applyDBSchema()
}

fun globalTeardown() {
    disconnect()
}
