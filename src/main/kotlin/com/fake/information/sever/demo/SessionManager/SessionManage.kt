package com.fake.information.sever.demo.SessionManager

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession


open class SessionManage(request: HttpServletRequest) {

    private var session: HttpSession = request.session

    open fun createSession(userId: String, status_Code: Any): HttpSession {
        session.setAttribute(session.id,status_Code)
        return session
    }
    fun deleteSession(){
        session.invalidate()
    }
    fun getSessionValue(sessionId:String):Any?{
        return session.getAttribute(sessionId)
    }
}