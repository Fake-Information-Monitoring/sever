package com.fake.information.sever.demo.Until.EmailUntil

interface IMailService {
    /**
     * 发送文本邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     */
    fun sendSimpleMail(to: String?, subject: String?, content: String?)

    /**
     * 发送HTML邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     */
    fun sendHtmlMail(to: String?, subject: String?, content: String?)

    /**
     * 发送带附件的邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @param filePath 附件
     */
    fun sendAttachmentsMail(to: String?, subject: String?, content: String?, filePath: String?)
}