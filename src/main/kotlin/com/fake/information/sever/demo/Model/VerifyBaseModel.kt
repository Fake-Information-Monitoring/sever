package com.fake.information.sever.demo.Model

import com.google.gson.annotations.SerializedName

class VerifyBaseModel{
    @SerializedName("code")
    val code: Int = 0

    @SerializedName("success")
    val success: Boolean = false
    @SerializedName("text")
    val text: String? = null
    @SerializedName("data")
    val data: HashMap<String,Any>? = null

    @SerializedName("isFake")
    val isFake: Boolean? = null
}