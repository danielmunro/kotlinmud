package kotlinmud.db

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.sql.Connection

fun createConnection() {
//    Database.connect("jdbc:sqlite:./data.db", "org.sqlite.JDBC")
//    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    Database.connect("jdbc:postgresql://localhost:5432/postgres", driver = "org.postgresql.Driver",
        user = "postgres", password = "postgres")
}
