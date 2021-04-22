package com.fake.information.sever.demo.Http.Until

import com.fake.information.sever.demo.Model.VerifyModel.RumorsModel
import com.fake.information.sever.demo.Model.VerifyModel.SensitiveModel
import com.fake.information.sever.demo.Model.VerifyModel.ZombiesModel
import kotlin.reflect.KClass

object VerifyResultFactory {
    fun getResultClass(type:String): Any {
        return when (type){
            "Rumor" -> RumorsModel()
            "Sensitive"-> SensitiveModel()
            "Zombies"-> ZombiesModel()
            else -> throw NullPointerException("不存在该类型")
        }
    }
}