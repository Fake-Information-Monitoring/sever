package com.fake.information.sever.demo.Until

import cn.hutool.core.lang.UUID.randomUUID

class UUID {
    companion object {
        fun getUuid(): String {
            return randomUUID().toString()
        }
    }
}