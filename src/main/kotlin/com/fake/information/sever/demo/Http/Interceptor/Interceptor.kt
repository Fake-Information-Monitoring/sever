package com.fake.information.sever.demo.Http.Interceptor

import com.fake.information.sever.demo.Redis.FakeNewsRedisTemplate
import com.fake.information.sever.demo.Http.Response.StatusCode
import com.fake.information.sever.demo.Http.Response.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
class Interceptor : WebMvcConfigurer {

    @Override
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(SecurityInterceptor(redisTemplate))
                .addPathPatterns("/**")
                //排除拦截
                .excludePathPatterns(arrayListOf(
                        "/v1/login/**",
                        "/v1/signUp/**",
                        "/v1/signUp/email",
                        "/v1/login/getPublicKey",
                        "/v1/cdKey/verify",
                        "/v1/login/verifyCode/{date}",
                        "/v1/forward/",
                        "/v1/forward/change",
                        "/v1/forward/sendEmail",
                        "/ImageEdge",
                        "/ImageEdgeByte"
                ))
        //拦截路径

    }

    @Autowired
    lateinit var redisTemplate: FakeNewsRedisTemplate

    @Configuration
    class SecurityInterceptor(val redisTemplate: FakeNewsRedisTemplate) : HandlerInterceptor {


        override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
            val session = request.session
            if (redisTemplate.getRedis(session.id) == StatusCode.Status200.statusCode) {//TODO: session状态码拦截
                return true
            }
            val result: Result<String> = Result(
                    success = false,
                    code = StatusCode.Status401.statusCode,
                    msg = "Bad Request",
                    data = "Please Login"
            )
            response.status=StatusCode.Status401.statusCode
            response.writer.print(result.toJson())
            return false
        }
    }
}