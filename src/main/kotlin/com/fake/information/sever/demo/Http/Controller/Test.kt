package com.fake.information.sever.demo.Http.Controller

import com.fake.information.sever.demo.DAO.UserRepository
import com.fake.information.sever.demo.Http.Response.Result
import com.fake.information.sever.demo.Model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.*
import java.net.http.HttpRequest
import java.time.DateTimeException
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession


@RestController
@RequestMapping("/test")
class Test {

    private lateinit var result: Result<HttpSession>
    @Autowired
    private lateinit var userRepository: UserRepository

    @RequestMapping("/add")
    fun saveUser(): String {
        val user = User()
        user.name ="userName"
        user.email = "email"
        user.gender = "gender"
        //user.password = "password"
        user.phoneNumber = 1233
        user.update = Date()
        userRepository.save(user)
        return "success"
    }
    @GetMapping("/hello")
    fun hello():String{
        return "hello"
    }
    @PostMapping("/create")
    fun testSession1(@RequestParam("userId") userId:String,httpRequest: HttpServletRequest,response: HttpServletResponse){
        var session : SessionController = SessionController(httpRequest)
        response.writer.print(session.createSession(userId,StatusCode.Status_200).id)
    }
    @PostMapping("/delete")
    fun testSession2(@RequestParam("userId") userId:String,httpRequest: HttpServletRequest,response: HttpServletResponse){
        var session : SessionController = SessionController(httpRequest)
        response.writer.println(session.createSession(userId,StatusCode.Status_200).id)
        response.writer.println(httpRequest.getSession().id)
        response.writer.println(session.checkSessionStatusCode(userId))
        response.writer.println(session.deleteSession())
    }

}