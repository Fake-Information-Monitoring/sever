package com.fake.information.sever.demo.Http.Controller

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession


class SessionController(request: HttpServletRequest) {

     private var session: HttpSession = request.session


    open fun createSession(userId: String, status_Code: StatusCode): HttpSession {
        session.setAttribute(userId,status_Code)
        return session
    }
    fun deleteSession(){
        session.invalidate()
    }
    fun checkSessionStatusCode(userId: String): Any? {
        return session.getAttribute(userId)
    }
}