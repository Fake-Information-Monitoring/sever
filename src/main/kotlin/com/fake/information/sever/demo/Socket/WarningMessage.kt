package com.fake.information.sever.demo.Socket

import cn.hutool.core.date.DateTime
import java.util.*

class WarningMessage(val name:String,val message:String) {
    val time = DateTime.now()
    override fun toString(): String {
        return buildString {
            append(time.toString()+"\t")
            append(name+"\t")
            append(message)
        }
    }


}