package kotlinmud.db

import org.jetbrains.exposed.sql.Database

fun createConnection() {
//    Database.connect("jdbc:postgresql://127.0.0.1:5432/kotlinmud", driver = "org.postgresql.Driver",
//        user = "kotlinmud", password = "kotlinmud")
//    Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
    Database.connect("jdbc:h2:./myh2file", "org.h2.Driver")
}
