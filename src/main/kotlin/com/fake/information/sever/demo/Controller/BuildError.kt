package com.fake.information.sever.demo.Controller

class BuildError{
    companion object {
        @ExperimentalStdlibApi
        fun buildErrorInfo(errorInfo: String): Map<String, Any> {
            return mapOf(
                "error" to errorInfo
            )
        }
    }
}