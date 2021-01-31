package kotlinmud.db

import java.sql.Connection
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager

fun createConnection() {
    Database.connect("jdbc:sqlite:./data.db", "org.sqlite.JDBC")
//    Database.connect("jdbc:postgresql://localhost:5432/kotlinmud", driver = "org.postgresql.Driver",
//        user = "kotlinmud", password = "kotlinmud")
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
}
