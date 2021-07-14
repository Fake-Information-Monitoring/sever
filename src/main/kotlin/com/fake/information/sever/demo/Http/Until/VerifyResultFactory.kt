package com.fake.information.sever.demo.Http.Until

import com.fake.information.sever.demo.DAO.FakeMessageInfoRepository
import com.fake.information.sever.demo.Http.Api.Response.TokenType
import com.fake.information.sever.demo.Model.FakeMessageInfo
import com.fake.information.sever.demo.Model.User
import com.fake.information.sever.demo.Model.VerifyBaseModel
import com.fake.information.sever.demo.Socket.WebSocketSever
import com.fake.information.sever.demo.Until.Requests.DemoOkhttp

object VerifyResultFactory {
    fun getResult(user: User? = null, type: String, message: FakeMessageInfo, UUID: String = "",fakeMessageInfoRepository: FakeMessageInfoRepository? = null): VerifyBaseModel<*> {
        val text = message.info.toString()
        val result = when (type) {
            TokenType.RUMORS.toString() -> DemoOkhttp.post<VerifyBaseModel<VerifyBaseModel.RumorsModel>>(
                header = mapOf(
                    "text" to text
                ),
                url = AISeverURL.RUMOR_URL.toString()
            )
            TokenType.SENSITIVE_WORD.toString() -> DemoOkhttp.post<VerifyBaseModel<VerifyBaseModel.SensitiveModel>>(
                header = mapOf(
                    "text" to text
                ),
                url = AISeverURL.SENSITIVE_WORD_URL.toString()
            )
            TokenType.ZOMBIES.toString() -> DemoOkhttp.post<VerifyBaseModel<VerifyBaseModel.ZombiesModel>>(
                header = mapOf(
                    "text" to text
                ),
                url = AISeverURL.ZOMBIES_URL.toString()
            )
            TokenType.DIY_MODEL.toString() -> {
                val body = mapOf(
                    "uuid" to UUID,
                    "text" to text
                )
                DemoOkhttp.post<VerifyBaseModel<VerifyBaseModel.DIYModel>>(
                    url = AISeverURL.MODEL_URL.toString(),
                    header = body
                )
            }
            else -> throw NullPointerException("不存在该类型")
        }
        if(user == null) return result
        if (result.isFake){
            fakeMessageInfoRepository!!.save(message)
            WebSocketSever.Sender.sendMessage(user,message)
        }
        return result
    }
}