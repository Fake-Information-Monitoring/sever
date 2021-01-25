package com.fake.information.sever.demo.Http.Controller.Api

import com.fake.information.sever.demo.DAO.CDKeyRepository
import com.fake.information.sever.demo.DAO.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/cdKey")
class CDKeyController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var cdKeyRepository: CDKeyRepository

    @PostMapping("/createKey")
    fun createKey(@RequestHeader("id") userId:Int){

    }
}