package com.fake.information.sever.demo.Http.Controller

class BuildError{
    companion object {
        @ExperimentalStdlibApi
        fun buildErrorInfo(errorInfo: String): String {
            return buildMap<String, String> {
                "error" to errorInfo
            }.toString()
        }
    }
}