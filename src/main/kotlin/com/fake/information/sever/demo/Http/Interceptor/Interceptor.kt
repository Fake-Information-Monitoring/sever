package com.fake.information.sever.demo.Http.Interceptor

import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession


class Interceptor : HandlerInterceptor {
    @Autowired
    private lateinit var stringRedisTemplate : StringRedisTemplate

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        var session : HttpSession = request.getSession()
        if (session.getAttribute("") != null){
            try {
                val loginSessionId : String? = stringRedisTemplate.opsForValue().get("" + session.getAttribute("") as Long)
                if (loginSessionId != null && loginSessionId.equals(session.id)){
                    return true
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }

        return false
    }
    fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(SecurityInterceptor()) //排除拦截
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/logout") //拦截路径
                .addPathPatterns("/**")
    }

    @Configuration
    class SecurityInterceptor : HandlerInterceptor {
        @Throws(IOException::class)
        override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
            val session = request.session
            if (session.getAttribute(session.id) != null) {
                return true
            }
            response.writer.write(Gson.(
                    object : BaseResponse() {
                        init {
                            setOk(false)
                            setMessage("please login first")
                        }
                    }
            ))
            return false
        }
    }
}