package com.fake.information.sever.demo.Until.OSS

import com.aliyun.oss.OSSClient
import com.aliyun.oss.OSSException

import com.aliyun.oss.model.CannedAccessControlList
import com.aliyun.oss.model.CreateBucketRequest
import com.aliyun.oss.model.PutObjectRequest
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class OSSUpload {

    companion object {

        fun upload(file: File?): String? {
            val endpoint = OSSConfiguration.OSS_END_POINT
            val accessKeyId = OSSConfiguration.OSS_ACCESS_KEY_ID
            val accessKeySecret = OSSConfiguration.OSS_ACCESS_KEY_SECRET
            val bucketName = OSSConfiguration.OSS_BUCKET_NAME
            val fileHost = OSSConfiguration.OSS_FILE_HOST
            val format = SimpleDateFormat("yyyy-MM-dd")
            val dateStr = format.format(Date())
            if (null == file) {
                return null
            }
            val ossClient = OSSClient(endpoint, accessKeyId, accessKeySecret)
            try {
                //容器不存在，就创建
                if (!ossClient.doesBucketExist(bucketName)) {
                    ossClient.createBucket(bucketName)
                    val createBucketRequest = CreateBucketRequest(bucketName)
                    createBucketRequest.cannedACL = CannedAccessControlList.PublicRead
                    ossClient.createBucket(createBucketRequest)
                }
                //修改文件名字
                val fileName = fileHost + "/" + (dateStr + "/" + UUID.randomUUID().toString().replace("-", "") + "-" + file.name)
                //创建文件路径
                val fileUrl = "https://$bucketName.$endpoint/$fileName"
                //上传文件
                val result = ossClient.putObject(PutObjectRequest(bucketName, fileName, file))
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