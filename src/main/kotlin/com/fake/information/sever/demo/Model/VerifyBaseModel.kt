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

    data class SensitiveModel(
        @SerializedName("反动信息")
        val 反动信息: Double = 0.0,
        @SerializedName("民生类违法信息")
        val 民生类违法信息: Double = 0.0,
        @SerializedName("广告类违法信息")
        val 广告类违法信息: Double = 0.0,
        @SerializedName("涉黄违法信息")
        val 涉黄违法信息: Double = 0.0,
        @SerializedName("暴恐类违法信息")
        val 暴恐类违法信息: Double = 0.0,
        @SerializedName("涉枪违法信息")
        val 涉枪违法信息: Double = 0.0,
        @SerializedName("政治敏感信息")
        val 政治敏感信息: Double = 0.0
    )

    data class ZombiesModel(

        @SerializedName("是僵尸用户")
        val isZombies: Double = 0.0,
        @SerializedName("是正常用户")
        val isNormal: Double = 0.0
    )
}