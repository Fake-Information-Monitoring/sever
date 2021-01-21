package com.fake.information.sever.demo.Http.Interceptor

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.web.servlet.HandlerInterceptor
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

class RedisSessionInterceptor : HandlerInterceptor {
    @Autowired
    private lateinit var stringRedisTemplate : StringRedisTemplate

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        var session : HttpSession = request.getSession()
        if (session.getAttribute("") != null){
            try {
                val loginSessionId : String? = stringRedisTemplate.opsForValue().get(""+ session.getAttribute("") as Long)
                if (loginSessionId != null && loginSessionId.equals(session.id)){
                    return true
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }


        return false
    }
    private fun response401(response :HttpServletResponse )
    {
        response.setCharacterEncoding("UTF-8")
        response.setContentType("application/json; charset=utf-8")

        try
        {
            //response.getWriter().print(JSON.toJSONString( ReturnData(StatusCode.NEED_LOGIN, "", "用户未登录！")))
        }
        catch (e: IOException)
        {
            e.printStackTrace()
        }
    }
}