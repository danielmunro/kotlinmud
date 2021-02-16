package kotlinmud.db

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.sql.Connection

fun createConnection() {
// sqlite
    Database.connect("jdbc:sqlite:./data.db", "org.sqlite.JDBC")
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

// postgres
//    Database.connect(
//        "jdbc:postgresql://localhost:5432/kotlinmud", driver = "org.postgresql.Driver",
//        user = "kotlinmud", password = "kotlinmud"
//    )

    // h2
//    Database.connect("jdbc:h2:./myh2file", driver = "org.h2.Driver", user = "root", password = "")
}
