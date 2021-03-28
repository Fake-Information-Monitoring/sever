package com.fake.information.sever.demo.Http.Until

import org.apache.commons.codec.binary.Base64
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.Cipher

object RSA{
    fun encryptByPublicKey(input: String, publicKey: PublicKey): String {
        //创建cipher对象
        val cipher = Cipher.getInstance("RSA")
        //初始化cipher
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        //加密/解密
        val encrypt = cipher.doFinal(input.toByteArray())
        return Base64.encodeBase64(encrypt).toString()
    }
    fun decryptByPrivateKey(input: String, privateKey: PrivateKey): String {
        //创建cipher对象
        val cipher = Cipher.getInstance("RSA")
        //初始化cipher
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        //加密/解密
        val encrypt = cipher.doFinal(Base64.decodeBase64(input))
        return String(encrypt)
    }
    fun getKeyPair(): KeyPair? {
        val generator=KeyPairGenerator.getInstance("RSA")          //密钥对生成器
        return generator.genKeyPair()
    }
}
