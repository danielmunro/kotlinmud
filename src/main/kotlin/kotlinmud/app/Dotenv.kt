package kotlinmud.app

import io.github.cdimascio.dotenv.Dotenv

fun getDotenv(): Dotenv {
    return Dotenv.configure()
        .ignoreIfMissing()
        .load()
}
