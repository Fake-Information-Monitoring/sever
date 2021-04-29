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
        val endpoint = OSSConfiguration.OSS_END_POINT
        val accessKeyId = OSSConfiguration.OSS_ACCESS_KEY_ID
        val accessKeySecret = OSSConfiguration.OSS_ACCESS_KEY_SECRET
        val bucketName: String? = OSSConfiguration.OSS_BUCKET_NAME
        val fileHost = OSSConfiguration.OSS_FILE_HOST
        val format = SimpleDateFormat("yyyy-MM-dd")
        val dateStr = format.format(Date())


        fun upload(input: InputStream?,name:String): String? {
            val ossClient = OSSConfiguration().getOSSClient()
            ossClient.clientConfiguration.socketTimeout = 100000
            try {
                //容器不存在，就创建
                if (!ossClient.doesBucketExist(bucketName)) {
                    ossClient.createBucket(bucketName)
                    val createBucketRequest = CreateBucketRequest(bucketName)
                    createBucketRequest.cannedACL = CannedAccessControlList.PublicRead
                    ossClient.createBucket(createBucketRequest)
                }
                //修改文件名字
                val fileName = "$fileHost/${dateStr}/${name}"
                //创建文件路径
                val fileUrl = "https://$bucketName.$endpoint/$fileName"
                //上传文件

                val result = ossClient.putObject(
                    PutObjectRequest(
                        bucketName!!,
                        fileName,
                        input
                    )
                )
                //设置权限 这里是公开读
                ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead)
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