package com.fake.information.sever.demo.Http.Api.Controller

import com.fake.information.sever.demo.Config.Redis.FakeNewsRedisTemplate
import com.fake.information.sever.demo.DAO.CDKeyRepository
import com.fake.information.sever.demo.DAO.UserRepository
import com.fake.information.sever.demo.Http.Api.Response.Result
import com.fake.information.sever.demo.Http.Api.Response.StatusCode
import com.fake.information.sever.demo.Http.Until.AISeverURL
import com.fake.information.sever.demo.Model.CDKey
import com.fake.information.sever.demo.Model.Commit
import com.fake.information.sever.demo.Model.ModelInfo
import com.fake.information.sever.demo.Until.AsyncTask.AsyncService
import com.fake.information.sever.demo.Until.JWT.TokenConfig
import com.fake.information.sever.demo.Until.OSS.OSSConfiguration
import com.fake.information.sever.demo.Until.OSS.OSSUpload
import com.fake.information.sever.demo.Until.Requests.DemoOkhttp.post
import com.fake.information.sever.demo.Until.UUID.UUID
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.util.*
import javax.servlet.http.HttpSession


@RestController
@RequestMapping("/v1/upload")
@Api("上传文件管理")
class UploadController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var redisTemplate: FakeNewsRedisTemplate

    @Autowired
    private lateinit var cdKeyRepository: CDKeyRepository

    @Autowired
    private lateinit var asyncService: AsyncService

    fun verifyToken(token: String): Boolean {
        val count = redisTemplate.getRedis(token + "nums").toString().toInt()
        redisTemplate.setRedis(token + "nums", count + 1)
        if (count >= TokenConfig.TOKEN_GET_COUNT) {
            return false
        }
        return true
    }

    @PostMapping("/addTrainModel")
    fun postAddTrainModel(
        @RequestBody file: MultipartFile,
        session: HttpSession,
        @RequestHeader("uuid") uuid: String,
        @RequestHeader("type") type: String
    ) {
        val b:ByteArray = file.bytes
        asyncService.asyncTask {
            val key = cdKeyRepository.findByKey(uuid)!!
            val model = ModelInfo()
            model.key = key
            model.modelName = type
            model.setModel(b)
            model.trainStatus = 1
            key.model = model
            cdKeyRepository.save(key)
        }
    }

    @PostMapping("/uploadFile")
    @ApiOperation("上传文件")
    fun postCommitFile(
        @RequestHeader("appName") appName: String,
        @RequestHeader("type") type: String,
        @RequestBody file: MultipartFile,
        session: HttpSession
    ): Result<String> {
        if (appName.isEmpty()) {
            throw Exception("应用不能为空")
        }
        val id = redisTemplate.getUserId(session)
        val user = userRepository.getOne(id)
        val key = CDKey()
        key.key = UUID.getUuid()
        val filename = "${key.key}.txt"
        val input = ByteArrayInputStream(file.bytes)
        asyncService.asyncTask {
            OSSUpload.upload(input, filename)
            println("上传成功！")
            val commit = Commit()
            commit.user = user
            key.keyName = appName
            user.keyList.add(key)
            key.user = user
            key.keyType = "Model"
            commit.indexOSSUrl = filename
            val model = ModelInfo()
            model.modelName = type
            model.key = key
            model.trainStatus = -1
            commit.commitTime = Date()
            user.commitList.add(commit)
            userRepository.save(user)
            redisTemplate.setRedis(key.key + "nums", 0)
            redisTemplate.setRedis(key.key + "type", key.keyType.toString())
            post(
                header = mapOf(
                    "url" to "https://${OSSConfiguration.OSS_BUCKET_NAME}.${OSSConfiguration.OSS_END_POINT}/" +
                            "${OSSConfiguration.OSS_FILE_HOST}/${OSSUpload.dateStr}/${filename}",
                    "type" to type,
                    "uuid" to key.key.toString()
                ), url = AISeverURL.TRAIN_URL.toString()
            )
        }
        return Result(
            success = true,
            code = StatusCode.Status200.statusCode,
            msg = key.key.toString(),
            data = "https://${OSSConfiguration.OSS_BUCKET_NAME}.${OSSConfiguration.OSS_END_POINT}/" +
                    "${OSSConfiguration.OSS_FILE_HOST}/${OSSUpload.dateStr}/${filename}",
        )
    }

    @PostMapping("/uploadImage")
    @ApiOperation("更新头像")
    fun postHeadImg(
        @RequestBody img: MultipartFile,
        session: HttpSession
    ): Result<String> {
        val id = redisTemplate.getUserId(session)
        val user = userRepository.getOne(id)
        user.avatar = img.originalFilename?.let { OSSUpload.upload(img.inputStream, it) }
        userRepository.save(user)
        return Result(
            success = true,
            code = StatusCode.Status200.statusCode,
            msg = "OK",
            data = user.avatar
        )
    }
}