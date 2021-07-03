package com.fake.information.sever.demo.Socket

import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import javax.websocket.OnClose
import javax.websocket.OnMessage
import javax.websocket.OnOpen
import javax.websocket.Session
import javax.websocket.server.PathParam
import javax.websocket.server.ServerEndpoint
import kotlin.collections.LinkedHashMap


@ServerEndpoint("/ws/Connect/{userId}")
@Component
@Slf4j
class WebSocketSever {
    object Clients{
        val clients = LinkedHashMap<Int, Session>()
    }
    private val messageQueue = HashMap<Int, Queue<WarningMessage>>()
    @OnOpen
    fun onOpen(session: Session, @PathParam("userId") param:Int){
        val user:Int =param
        Clients.clients[user] = session
        println("用户${param}连接成功")
        if(messageQueue[user]!=null){
                messageQueue[user]?.forEach{
                    session.asyncRemote.sendText(it.toString())
                    println("发送报警")
                }
        }else{
            messageQueue[user] = LinkedBlockingQueue()
        }
    }

    @OnClose
    fun onClose(session: Session) {
        Clients.clients.forEach {
            if (it.value.id == session.id){
                Clients.clients.remove(it.key)
                return@forEach
            }
        }
    }

    @OnMessage
    fun onMessage(message: String?) {
        println(message)
    }
}