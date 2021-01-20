package com.fake.information.sever.demo.Controller

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