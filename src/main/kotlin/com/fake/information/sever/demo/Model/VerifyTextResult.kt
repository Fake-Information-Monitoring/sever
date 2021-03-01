package com.fake.information.sever.demo.Model


import com.google.gson.annotations.SerializedName

data class VerifyTextResult(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("success")
    val success: Boolean
) {
    data class Data(
        @SerializedName("广告类违法信息")
        val advertising: Double,
        @SerializedName("政治敏感信息")
        val political: Double,
        @SerializedName("暴恐类违法信息")
        val fear: Double,
        @SerializedName("正常发言")
        val normal: Double,
        @SerializedName("民生类违法信息")
        val human: Double,
        @SerializedName("涉枪违法信息")
        val gun_fear: Double,
        @SerializedName("涉黄违法信息")
        val sex: Double,
        @SerializedName("谣言")
        val rumor: Double
    )
}