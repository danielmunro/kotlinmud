package kotlinmud.db

import org.jetbrains.exposed.sql.Database

lateinit var db: Database

fun connect() {
    db = Database.connect("jdbc:postgresql://localhost:54321/postgres", driver = "org.postgresql.Driver",
        user = "postgres", password = "mysecretpassword")
}

fun disconnect() {
    db.connector().close()
}
