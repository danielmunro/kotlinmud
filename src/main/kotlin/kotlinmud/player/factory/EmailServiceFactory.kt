package kotlinmud.player.factory

import com.commit451.mailgun.SendMessageResponse
import io.mockk.every
import io.mockk.mockk
import kotlinmud.app.getDotenv
import kotlinmud.app.getMailgunClient
import kotlinmud.player.service.EmailService

fun createEmailServiceMock(): EmailService {
    val mock = mockk<EmailService>()
    every { mock.sendEmail(request = any()) } returns SendMessageResponse()
    return mock
}

fun createEmailService(): EmailService {
    val dotenv = getDotenv()
    val domain = dotenv["MAILGUN_DOMAIN"] ?: ""
    val apiKey = dotenv["MAILGUN_API_KEY"] ?: ""
    return EmailService(getMailgunClient(domain, apiKey))
}
