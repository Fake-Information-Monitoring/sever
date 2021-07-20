package com.fake.information.sever.demo.Http.Api.Controller

import cn.hutool.json.JSONObject
import com.fake.information.sever.demo.Config.Redis.FakeNewsRedisTemplate
import com.fake.information.sever.demo.Controller.tools.Check
import com.fake.information.sever.demo.DAO.*
import com.fake.information.sever.demo.Http.Api.Response.StatusCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import com.fake.information.sever.demo.Http.Api.Response.Result
import com.fake.information.sever.demo.Model.*
import com.fake.information.sever.demo.Until.AsyncTask.AsyncService
import com.fake.information.sever.demo.Until.OSS.OSSUpload
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.web.multipart.MultipartFile
import java.util.*
import javax.security.auth.login.AccountNotFoundException
import javax.servlet.http.HttpSession
import kotlin.NoSuchElementException
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@Api(value = "用户信息管理接口")
@RestController
@RequestMapping("/v1/getInfo")
class UserInfoController {
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var cdKeyRepository: CDKeyRepository
    @Autowired
    private lateinit var warningFakeMessageInfoRepository: FakeMessageInfoRepository
    @Autowired
    private lateinit var commitRepository: CommitRepository

    @Autowired
    private lateinit var redisTemplate: FakeNewsRedisTemplate

    @Autowired
    private lateinit var personCertifiedRepository: PersonCertifiedRepository

    @Autowired
    private lateinit var coroutinesRepository: CorporateCertifiedRepository

    @Autowired
    private lateinit var certificatePoolRepository: CertifiedPoolRepository

    @Autowired
    private lateinit var asyncService: AsyncService

    @PostMapping("/corporateCertified")
    @ApiOperation("个人认证信息")
    fun corporateCertified(
        @RequestParam("license") license: MultipartFile,
        @RequestParam("name") name: String,
        @RequestParam("phone") phone: String,
        @RequestParam("email") email: String,
        session: HttpSession
    ): Result<String> {
        val user = userRepository.findById(
            redisTemplate.getUserId(session)
        ).get()
        if (user.ceritifiedID != -1 && user.ceritifiedID != null) {
            throw AccountNotFoundException("您已认证")
        }
        val corporate = CorporateCertified()
//        asyncService.asyncTask {
            corporate.license = license.originalFilename?.let { OSSUpload.upload(license.inputStream, it) }
            corporate.email = email
            corporate.name = name
            corporate.phone = phone
            corporate.user = user
            coroutinesRepository.save(corporate)
            user.ceritifiedType = 0
            user.ceritifiedID = corporate.id
            userRepository.save(user)
            val certifiedPool = CertifiedPool()
            certifiedPool.setUser(user)
            certificatePoolRepository.save(certifiedPool)
//        }
        return Result(
            success = true,
            code = StatusCode.Status200.statusCode,
            msg = "success",
            data = "认证已提交，请等待审核"
        )
    }

    @PostMapping("/personCertified")
    @ApiOperation("个人认证信息")
    fun personCertified(
        @RequestBody param: Map<String, Any>,
        session: HttpSession
    ): Result<String> {
        val user = userRepository.findById(
            redisTemplate.getUserId(session)
        ).get()
        if (user.ceritifiedID != -1 && user.ceritifiedID != null) {
            throw AccountNotFoundException("您已认证")
        }

        val cardId = param["cardId"].toString()
        if (!Check.checkCardId(cardId)) {
            throw IllegalArgumentException("身份证号格式有误")
        }
        val work = param["work"].toString()
        val name = param["name"].toString()
        val personCertified = PersonCertified()
        asyncService.asyncTask {
            personCertified.user = user
            personCertified.name = name
            personCertified.cardId = cardId
            personCertified.work = work
            personCertifiedRepository.save(personCertified)
            user.ceritifiedID = personCertified.id
            user.ceritifiedType = 1
            userRepository.save(user)
            val certifiedPool = CertifiedPool()
            certifiedPool.setUser(user)
            certificatePoolRepository.save(certifiedPool)
        }
        return Result(
            success = true,
            code = StatusCode.Status200.statusCode,
            msg = "success",
            data = "认证已提交，请等待审核"
        )
    }
    @GetMapping("/KeyWarningInfo/{keyId}")
    @ExperimentalStdlibApi
    @ApiOperation("获取所有的报警信息")
    fun getTokenWarningInfo(session: HttpSession,@PathVariable keyId:Int): Any {
        return try {
            Result(
                success = true,
                code = StatusCode.Status200.statusCode,
                msg = "success",
                data = cdKeyRepository.findById(keyId).get().fakeMessageInfoList
            )

        } catch (e: NoSuchElementException) {
            Result<String>(
                success = false,
                code = StatusCode.Status502.statusCode,
                msg = e.toString()
            )
        }
    }

    @GetMapping("/WarningInfo/time/{keyId}")
    @ApiOperation("分时段报警信息")
    fun getWarningInfoByTime(session: HttpSession,@PathVariable keyId: Int):Any{
        val infoList = cdKeyRepository.findById(keyId).get().fakeMessageInfoList
        val timeObjs = HashMap<String,ArrayList<FakeMessageInfo>>()
        if(infoList?.isNotEmpty() == true){
            infoList.forEach { it
                val calendar = Calendar.getInstance()
                calendar.time = it.time
                val date = "${calendar.get(Calendar.YEAR)}年" +
                        "${calendar.get(Calendar.MONTH)}月" +
                        "${calendar.get(Calendar.DAY_OF_MONTH)}日"
                if (!timeObjs.containsKey(date)){
                    timeObjs[date] = ArrayList()
                }
                timeObjs[date]?.add(it)
            }
        }
        return Result<String>(
            success = true,
            code = StatusCode.Status200.statusCode,
            msg = timeObjs
        )
    }

    @GetMapping("/WarningInfo")
    @ExperimentalStdlibApi
    @ApiOperation("获取所有的报警信息")
    fun getWarningInfo(session: HttpSession): Any {
        return try {
            val user = redisTemplate.getUserId(session)
            val info:MutableMap<CDKey,MutableList<FakeMessageInfo>?> = HashMap()
            userRepository.findById(user).get().keyList.forEach {
                info[it] = it.fakeMessageInfoList
            }
            Result(
                success = true,
                code = StatusCode.Status200.statusCode,
                msg = "success",
                data = info
            )

        } catch (e: NoSuchElementException) {
            Result<String>(
                success = false,
                code = StatusCode.Status502.statusCode,
                msg = e.toString()
            )
        }
    }
    @GetMapping("/")
    @ApiOperation("获取用户信息")
    fun getUserInfo(
        session: HttpSession
    ): Any {
        return try {
            val user = redisTemplate.getUserId(session)
            Result(
                success = true,
                code = StatusCode.Status200.statusCode,
                msg = "success",
                data = userRepository.findById(user).get()
            )

        } catch (e: NoSuchElementException) {
            Result<String>(
                success = false,
                code = StatusCode.Status502.statusCode,
                msg = e.toString()
            )
        }
    }

    @GetMapping("/avatar")
    @ApiOperation("获取用户头像OSSURL")
    fun getUserAvatar(
        session: HttpSession
    ): Any {
        return try {
            val user = redisTemplate.getUserId(session)
            Result(
                success = true,
                code = StatusCode.Status200.statusCode,
                msg = "success",
                data = userRepository.getOne(user).avatar!!
            )
        } catch (e: NoSuchElementException) {
            Result(
                success = false,
                code = StatusCode.Status502.statusCode,
                msg = e.toString()
            )
        }
    }

    @GetMapping("/getUserKey")
    @ApiOperation("获取用户Key")
    fun getKey(
        session: HttpSession
    ): Result<String> {
        val user: User = userRepository.getOne(redisTemplate.getUserId(session))
        return Result<String>(
            success = true,
            code = StatusCode.Status200.statusCode,
            msg = user.keyList
        )
    }

    @ExperimentalStdlibApi
    @GetMapping("/commit")
    @ApiOperation("获取某个特定的提交验证记录")
    fun getCommit(
        commit: Int,
        session: HttpSession
    ): Any {
        return try {
            val user = redisTemplate.getUserId(session)
            val commit = commitRepository.getOne(commit)
            if (commit.user?.id == user)
                commit.indexOSSUrl!!
            else
                throw IllegalAccessException("您没有权限")
        } catch (e: NoSuchElementException) {
            Result<String>(
                success = false,
                code = StatusCode.Status502.statusCode,
                msg = e.toString()
            )
        }
    }
}