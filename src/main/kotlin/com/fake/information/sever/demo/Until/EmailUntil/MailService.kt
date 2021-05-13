package com.fake.information.sever.demo.Until.EmailUntil

import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.util.logging.Logger


@Service
class MailService : IMailService {
    @Autowired
    lateinit var mailSender: JavaMailSender

    @Value("\${mail.from}")
    lateinit var from: String

    @ObsoleteCoroutinesApi
    override fun sendSimpleMail(to: String?, subject: String?, content: String?) {
        val message = mailSender.createMimeMessage()
        val email = MimeMessageHelper(message, true)
        email.setFrom(from)
        email.setTo(to!!)
        email.setSubject(subject!!)
        email.setText(content!!)
        mailSender.send(message)
        println("send by $to $message")
    }

    override fun sendHtmlMail(to: String?, subject: String?, content: String?) {
    }

    override fun sendAttachmentsMail(to: String?, subject: String?, content: String?, filePath: String?) {
    }
}