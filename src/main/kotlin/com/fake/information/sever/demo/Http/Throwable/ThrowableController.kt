package com.fake.information.sever.demo.Http.Throwable

import com.fake.information.sever.demo.Http.Api.Response.StatusCode
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import com.fake.information.sever.demo.Http.Api.Response.Result
import javax.servlet.http.HttpServletResponse

@RestControllerAdvice
class ThrowableController {
    private val logger = LoggerFactory.getLogger(ThrowableController::class.java)

    @ExceptionHandler(Exception::class)
    @ResponseBody
    fun throwException(ex: Exception, request: WebRequest,response: HttpServletResponse): Result<Any>? {
        logger.error(ex.message)
        response.status = StatusCode.Status403.statusCode
        return ex.message?.let {
            Result(
                    success = false,
                    code = StatusCode.Status403.statusCode,
                    msg = it
            )
        }
    }
}