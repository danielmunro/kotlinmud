package kotlinmud.db

import org.jetbrains.exposed.sql.Database

fun connect() {
    Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver", user = "root", password = "")
}