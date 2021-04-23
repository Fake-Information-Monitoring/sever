package com.fake.information.sever.demo.Http.Until

import com.fake.information.sever.demo.Model.VerifyModel.RumorsModel
import com.fake.information.sever.demo.Model.VerifyModel.SensitiveModel
import com.fake.information.sever.demo.Model.VerifyModel.VerifyBaseModel
import com.fake.information.sever.demo.Model.VerifyModel.ZombiesModel
import com.fake.information.sever.demo.Until.Requests.DemoOkhttp
import kotlin.reflect.KClass

object VerifyResultFactory {
    fun getResult(type: String, text: String): VerifyBaseModel {
        return when (type) {
            "Rumor" -> DemoOkhttp.post<RumorsModel>(
                text = text,
                url = AISeverURL.RUMOR_URL.toString()
            )
            "Sensitive" -> DemoOkhttp.post<SensitiveModel>(
                text = text,
                url = AISeverURL.RUMOR_URL.toString()
            )
            "Zombies" -> DemoOkhttp.post<ZombiesModel>(
                text = text,
                url = AISeverURL.RUMOR_URL.toString()
            )
            else -> throw NullPointerException("不存在该类型")
        }
    }
}