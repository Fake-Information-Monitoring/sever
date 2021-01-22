package com.fake.information.sever.demo.Http.Interceptor

import com.fake.information.sever.demo.Http.Controller.StatusCode
import com.fake.information.sever.demo.Http.Response.Result
import com.google.gson.Gson
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
class Interceptor : WebMvcConfigurer {

    @Override
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(SecurityInterceptor())
                //排除拦截
                .excludePathPatterns("/login")
                .excludePathPatterns("/logout")
                .excludePathPatterns("/test/create")
                //拦截路径
                .addPathPatterns("/**")
    }

    @Configuration
    class SecurityInterceptor : HandlerInterceptor {
        override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
            val session = request.session

            if (session.getAttribute(session.id) == StatusCode.Status_200) {//TODO: session状态码拦截
                return true
            }
            val gson: Gson = Gson()
            val result: Result<String> = Result(
                    success = false,
                    code = 401,
                    msg = "Bad Request",
                    data = "Please Login"
            )
            response.writer.print(gson.toJson(result))
            return false
        }
    }
}