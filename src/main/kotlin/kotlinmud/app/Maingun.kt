package kotlinmud.app

import com.commit451.mailgun.Mailgun

fun getMailgunClient(domain: String, apiKey: String): Mailgun {
    return Mailgun.Builder(domain, apiKey).build()
}
