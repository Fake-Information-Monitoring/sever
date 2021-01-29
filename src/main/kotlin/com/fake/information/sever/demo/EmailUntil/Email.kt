package com.fake.information.sever.demo.EmailUntil

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service


@Service
class Email : IMailService {
    @Autowired
    private val mailSender: JavaMailSender? = null

    @Value("\${mail.from}")
    private val from: String? = null

    override fun sendSimpleMail(to: String?, subject: String?, content: String?) {
        Thread {
            val message = mailSender?.createMimeMessage()
            val email = MimeMessageHelper(message!!, true)
            email.setFrom(from!!)
            email.setTo(to!!)
            email.setSubject(subject!!)
            email.setText(content!!)
            mailSender?.send(message)
        }.start()
    }
    override fun sendHtmlMail(to: String?, subject: String?, content: String?) {
        TODO("Not yet implemented")
    }
    override fun sendAttachmentsMail(to: String?, subject: String?, content: String?, filePath: String?) {
        TODO("Not yet implemented")
    }
}