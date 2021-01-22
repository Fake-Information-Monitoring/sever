package com.fake.information.sever.demo.Http.Controller


import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession


class SessionController(request: HttpServletRequest) {

     private var session: HttpSession = request.session


    fun createSession(userId: String, status_Code: StatusCode): HttpSession {
        session.setAttribute(userId,status_Code)
        return session
    }
    fun deleteSession(){
        session.removeAttribute(session.id)
    }
    fun checkSession(){

    }
}