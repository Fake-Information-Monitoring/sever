package com.fake.information.sever.demo.Model.VerifyModel

import com.google.gson.annotations.SerializedName

data class SensitiveModel(
    @SerializedName("data")
    val data: String = "",
) : VerifyBaseModel()
