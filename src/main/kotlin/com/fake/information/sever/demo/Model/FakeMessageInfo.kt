package com.fake.information.sever.demo.Model

import cn.hutool.json.JSONObject
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.*
@Entity
@Table(name = "fake_message_info")
@JsonIgnoreProperties(value = ["cdKey"])
class FakeMessageInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = 0

    @ManyToOne(targetEntity = CDKey::class,cascade = [CascadeType.ALL],optional = false)
    @JoinColumn(name = "key_id",referencedColumnName = "id")
    var cdKey:CDKey? = null

    @Column(name = "worse_account")
    var account:String? = null
    @Column(name = "worse_name")
    var name:String? = null
    @OptIn(ExperimentalStdlibApi::class)
    fun toJsonString(): String {
        val result = JSONObject()
        result["account"] = account
        result["name"] = name
        result["info"] = info
        result["type"] = type
        result["appId"] = cdKey?.id
        result["words"] = words?.split(",")
        result["time"] = time
        return result.toString()
    }
    @Column(name = "worse_info")
    var info:String? = null
    @Column(name = "worse_type")
    var type:String? = null
    @Column(name = "time")
    var time: Date? = null
    @Column(name = "words",nullable = true)
    var words:String? = null
}