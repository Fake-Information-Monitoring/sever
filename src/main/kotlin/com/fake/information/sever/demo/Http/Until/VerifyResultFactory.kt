package com.fake.information.sever.demo.Http.Until

import com.fake.information.sever.demo.Http.Api.Response.TokenType
import com.fake.information.sever.demo.Model.VerifyBaseModel
import com.fake.information.sever.demo.Until.Requests.DemoOkhttp

object VerifyResultFactory {
    fun getResult(type: String, text: String): VerifyBaseModel<*> {
        return when (type) {
            TokenType.RUMORS.toString() -> DemoOkhttp.post<VerifyBaseModel<VerifyBaseModel.RumorsModel>>(
                text = text,
                url = AISeverURL.RUMOR_URL.toString()
            )
            TokenType.SENSITIVE_WORD.toString() -> DemoOkhttp.post<VerifyBaseModel<VerifyBaseModel.SensitiveModel>>(
                text = text,
                url = AISeverURL.SENSITIVE_WORD_URL.toString()
            )
            TokenType.ZOMBIES.toString() -> DemoOkhttp.post<VerifyBaseModel<VerifyBaseModel.ZombiesModel>>(
                text = text,
                url = AISeverURL.ZOMBIES_URL.toString()
            )
            else -> throw NullPointerException("不存在该类型")
        }
    }
}