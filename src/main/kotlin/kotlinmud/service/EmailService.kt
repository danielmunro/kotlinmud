package kotlinmud.service

import com.commit451.mailgun.Mailgun
import com.commit451.mailgun.SendMessageRequest
import com.commit451.mailgun.SendMessageResponse

class EmailService(private val mailgun: Mailgun) {
    fun sendEmail(request: SendMessageRequest): SendMessageResponse {
        return mailgun.sendMessage(request).blockingGet()
    }
}
