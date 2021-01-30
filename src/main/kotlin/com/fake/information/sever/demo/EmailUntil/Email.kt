package com.fake.information.sever.demo.EmailUntil

import com.fake.information.sever.demo.ThreadPool.FakeNewsThreadPool
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import javax.annotation.security.RunAs


@Service
class MailService : IMailService {
    @Autowired
    lateinit var mailSender: JavaMailSender

    @Value("\${mail.from}")
     lateinit var from: String

    @ObsoleteCoroutinesApi
    override fun sendSimpleMail(to: String?, subject: String?, content: String?) {
        GlobalScope.launch(FakeNewsThreadPool.threadPool) {
            val message = mailSender.createMimeMessage()
            val email = MimeMessageHelper(message, true)
            email.setFrom(from)
            email.setTo(to!!)
            email.setSubject(subject!!)
            email.setText(content!!)
            mailSender.send(message)
        }
    }
    override fun sendHtmlMail(to: String?, subject: String?, content: String?) {
        TODO("Not yet implemented")
    }
    override fun sendAttachmentsMail(to: String?, subject: String?, content: String?, filePath: String?) {
        TODO("Not yet implemented")
    }
}

fun main() {
    @SpringBootTest
    class MailServiceTest{
        @Autowired lateinit var email: MailService
        @Test
        fun testSend(){
            email.sendSimpleMail("735647571@qq.com","Test","No Body Knows Fake News Better Than Me")
        }
    }
}