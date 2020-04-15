package kotlinmud.player

import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

const val SALT_ALGORITHM = "PBKDF2WithHmacSHA1"
const val SALT_LENGTH = 16
const val ITERATIONS = 65536
const val KEY_LENGTH = 128

fun generateSalt(password: String): ByteArray {
    val random = SecureRandom()
    val salt = ByteArray(SALT_LENGTH)
    random.nextBytes(salt)

    val spec: KeySpec = PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH)
    val factory = SecretKeyFactory.getInstance(SALT_ALGORITHM)

    return factory.generateSecret(spec).encoded
}
