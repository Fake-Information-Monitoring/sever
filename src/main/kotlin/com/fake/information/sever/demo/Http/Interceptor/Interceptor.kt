package com.fake.information.sever.demo.Http.Interceptor

import com.fake.information.sever.demo.Http.Controller.StatusCode
import com.fake.information.sever.demo.Http.Response.Result
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
        registry.addInterceptor(SecurityInterceptor())
                .addPathPatterns("/**")
                //排除拦截
                .excludePathPatterns(arrayListOf(
                        "/v1/login/",
                        "/v1/login/loginWithPhone",
                        "/v1/signUp/create",
                        "/v1/signUp/email",
                        "/v1/login/getPublicKey",
                        "/v1/cdKey/verify",
                        "/v1/login/verifyCode"
                ))
                //拦截路径

    }

    @Configuration
    class SecurityInterceptor : HandlerInterceptor {
        override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
            val session = request.session
            if (session.getAttribute(session.id) == StatusCode.Status_200.statusCode) {//TODO: session状态码拦截
                return true
            }
            val result: Result<String> = Result(
                    success = false,
                    code = StatusCode.Status_401.statusCode,
                    msg = "Bad Request",
                    data = "Please Login"
            )
            response.writer.print(result.toJson())
            return false
        }
    }
}