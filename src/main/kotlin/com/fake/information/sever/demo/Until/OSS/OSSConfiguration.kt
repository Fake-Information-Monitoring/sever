package com.fake.information.sever.demo.Until.OSS

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component

class OSSConfiguration : InitializingBean {

    @Value("\${oss.file.endpoint}")
    private val oss_file_endpoint: String? = null

    @Value("\${oss.file.keyid}")
    private val oss_file_keyid: String? = null

    @Value("\${oss.file.keysecret}")
    private val oss_file_keysecret: String? = null

    @Value("\${oss.file.filehost}")
    private val oss_file_filehost: String? = null

    @Value("\${oss.file.bucketname}")
    private val oss_file_bucketname: String? = null

    companion object{
        public var OSS_END_POINT: String? = null
        public var OSS_ACCESS_KEY_ID: String? = null
        public var OSS_ACCESS_KEY_SECRET: String? = null
        public var OSS_BUCKET_NAME: String? = null
        public var OSS_FILE_HOST: String? = null
    }

    @Throws(Exception::class)
    override fun afterPropertiesSet() {
        OSS_END_POINT = oss_file_endpoint
        OSS_ACCESS_KEY_ID = oss_file_keyid
        OSS_ACCESS_KEY_SECRET = oss_file_keysecret
        OSS_BUCKET_NAME = oss_file_bucketname
        OSS_FILE_HOST = oss_file_filehost
    }
}