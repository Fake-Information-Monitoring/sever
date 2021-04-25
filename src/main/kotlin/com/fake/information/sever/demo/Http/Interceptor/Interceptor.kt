package com.fake.information.sever.demo.Http.Interceptor

import com.fake.information.sever.demo.Config.Redis.FakeNewsRedisTemplate
import com.fake.information.sever.demo.Http.Api.Response.StatusCode
import com.fake.information.sever.demo.Http.Api.Response.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.time.ExperimentalTime

@Configuration
class Interceptor : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .exposedHeaders(
                "access-control-allow-headers",
                "access-control-allow-methods",
                "access-control-allow-origin",
                "access-control-max-age",
                "Authorization",
                "X-Frame-Options"
            )
            .allowCredentials(false).maxAge(3600);
    }

    @Override
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(SecurityInterceptor(redisTemplate))
            .addPathPatterns("/**")
            //排除拦截
            .excludePathPatterns(
                arrayListOf(
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
                    "/v1/FakeNewsVerify/",
                    "/ImageEdgeByte",
                    "/swagger-ui.html",
                    "/swagger-ui.html/**",
                    "/webjars/**",
                    "/error",
                    "/swagger-resources",
                    "/swagger-resources/**",
                    "/csrf",
                    "/v1/FakeNewsVerify/testVerify"
                )
            )
        //拦截路径

    }

    @Autowired
    lateinit var redisTemplate: FakeNewsRedisTemplate

    @Configuration
    class SecurityInterceptor(val redisTemplate: FakeNewsRedisTemplate) : HandlerInterceptor {


        @ExperimentalTime
        override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
            response.addHeader("Access-Control-Allow-Credentials", "true")
            val session = request.session
            if (redisTemplate.getRedis(session.id) == StatusCode.Status200.statusCode) {
                return true
            }
            val result: Result<String> = Result(
                success = false,
                code = StatusCode.Status302.statusCode,
                msg = "Bad Request",
                data = "Please Login"
            )
            response.status = StatusCode.Status302.statusCode
            response.writer.print(result.toJson())
            return false
        }
    }
}