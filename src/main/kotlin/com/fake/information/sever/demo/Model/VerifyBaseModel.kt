package com.fake.information.sever.demo.Model

import com.google.gson.annotations.SerializedName

data class VerifyBaseModel<T>(
    @SerializedName("code")
    val code: Int = 0,
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("data")
    val data: T,
) {
    data class RumorsModel(
        @SerializedName("谣言")
        val rumor: Double = 0.0,
        @SerializedName("非谣言")
        val nonRumor: Double = 0.0
    )

    data class DIYModel(
        @SerializedName("result")
        val result: Result,
        @SerializedName("text")
        val text: String = ""
    ) {
        data class Result(
            @SerializedName("words")
            val words: List<String>?,
            @SerializedName("type")
            val type: String = ""
        )
    }

    data class SensitiveModel(
        @SerializedName("result")
        val result: Result,
        @SerializedName("text")
        val text: String = ""
    ) {
        data class Result(
            @SerializedName("政治反动信息")
            val 政治反动信息: List<String>?,
            @SerializedName("广告信息")
            val 广告信息: List<String>?,
            @SerializedName("暴恐信息")
            val 暴恐信息: List<String>?,
            @SerializedName("淫秽色情信息")
            val 淫秽色情信息: List<String>?,
            @SerializedName("政治敏感信息")
            val 政治敏感信息: List<String>?,
            @SerializedName("枪械暴乱信息")
            val 枪械暴乱信息: List<String>?,
            @SerializedName("民生纠纷信息")
            val 民生纠纷信息: List<String>?
        )
    }

    data class ZombiesModel(

        @SerializedName("是僵尸用户")
        val isZombies: Double = 0.0,
        @SerializedName("是正常用户")
        val isNormal: Double = 0.0
    )
}