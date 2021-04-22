package com.fake.information.sever.demo.Model.VerifyModel

import com.google.gson.annotations.SerializedName

abstract class VerifyBaseModel (@SerializedName("code")
                            val code: Int = 0,
                            @SerializedName("success")
                            val success: Boolean = false)