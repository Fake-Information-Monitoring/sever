package com.fake.information.sever.demo.Controller.tools

class BuildError {
    companion object {
        @ExperimentalStdlibApi
        fun buildErrorInfo(errorInfo: String): Map<String, Any> {
            return mapOf(
                    "status" to "error",
                    "error" to errorInfo
            )
        }
    }
}