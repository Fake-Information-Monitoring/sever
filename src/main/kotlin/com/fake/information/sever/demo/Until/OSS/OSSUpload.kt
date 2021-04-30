package com.fake.information.sever.demo.Until.OSS

import com.aliyun.oss.OSSClient
import com.aliyun.oss.OSSException
import com.aliyun.oss.model.CannedAccessControlList
import com.aliyun.oss.model.CreateBucketRequest
import com.aliyun.oss.model.PutObjectRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*


@Component
class OSSUpload {

    companion object {
        val format = SimpleDateFormat("yyyy-MM-dd")
        val dateStr = format.format(Date())

        fun upload(input: InputStream?, name: String): String? {
            val ossConfiguration = OSSConfiguration()
            val ossClient = ossConfiguration.getOSSClient()
            ossClient.clientConfiguration.socketTimeout = 100000
            try {
                //容器不存在，就创建
                if (!ossClient.doesBucketExist(OSSConfiguration.OSS_BUCKET_NAME)) {
                    ossClient.createBucket(OSSConfiguration.OSS_BUCKET_NAME)
                    val createBucketRequest = CreateBucketRequest(OSSConfiguration.OSS_BUCKET_NAME)
                    createBucketRequest.cannedACL = CannedAccessControlList.PublicRead
                    ossClient.createBucket(createBucketRequest)
                }
                //修改文件名字
                val fileName = "${OSSConfiguration.OSS_FILE_HOST}/${dateStr}/${name}"
                //创建文件路径
                val fileUrl = "https://${OSSConfiguration.OSS_BUCKET_NAME}${OSSConfiguration.OSS_END_POINT}/$fileName"
                //上传文件

                val result = ossClient.putObject(
                    PutObjectRequest(
                        OSSConfiguration.OSS_BUCKET_NAME!!,
                        fileName,
                        input
                    )
                )
                //设置权限 这里是公开读
                ossClient.setBucketAcl(OSSConfiguration.OSS_BUCKET_NAME, CannedAccessControlList.PublicRead)
                if (null != result) {
                    return fileUrl
                }
            } catch (ignored: OSSException) {
            } finally {
                //关闭
                ossClient.shutdown()
            }
            return null
        }
    }
}