package com.fake.information.sever.demo.Http.Throwable

import com.fake.information.sever.demo.Http.Response.StatusCode
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import com.fake.information.sever.demo.Http.Response.Result

@RestControllerAdvice
class ThrowableController {
    private val logger = LoggerFactory.getLogger(ThrowableController::class.java)

    @ExceptionHandler(Exception::class)
    @ResponseBody
    fun throwException(ex: Exception, request: WebRequest): Result<Any>? {
        logger.error(ex.message)
        return ex.message?.let {
            Result<Any>(
                    success = false,
                    code = StatusCode.Status502.statusCode,
                    msg = it
            )
        }
    }
}