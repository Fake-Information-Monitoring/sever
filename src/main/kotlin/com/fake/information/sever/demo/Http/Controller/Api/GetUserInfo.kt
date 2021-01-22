package com.fake.information.sever.demo.Controller

import com.fake.information.sever.demo.Controller.tools.BuildError
import com.fake.information.sever.demo.DAO.CommitRepository
import com.fake.information.sever.demo.DAO.UserRepository
import com.fake.information.sever.demo.Model.Commit
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.io.File

@RestController
@RequestMapping("/v1/getInfo",method = [ RequestMethod.GET])
class GetUserInfo {
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var commitRepository: CommitRepository
    @ExperimentalStdlibApi
    @PostMapping("/{user}")
    fun userInfo(@PathVariable user:Int): Map<String, Any?> {
         return userRepository.findById(user).get().getIndex()
    }
    @PostMapping("/{user}/avatar")
    fun userAvatar(@PathVariable user:Int): File? {
        return userRepository.getOne(user).avatar?.headImg
    }
    @ExperimentalStdlibApi
    @PostMapping("/{user}/{commit}")
    fun getCommit(@PathVariable user:Int,@PathVariable commit:Int): Any {
        val commit = commitRepository.getOne(commit)
        if (commit.user?.id == user){
            return commit.index?.index!!
        }
        return BuildError.buildErrorInfo("提交不存在")
    }
}