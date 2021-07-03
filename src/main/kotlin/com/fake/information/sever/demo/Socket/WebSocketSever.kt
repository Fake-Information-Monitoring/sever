package com.fake.information.sever.demo.Socket

import com.fake.information.sever.demo.DAO.UserRepository
import com.fake.information.sever.demo.Model.User
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*
import javax.websocket.server.ServerEndpoint
import java.util.concurrent.ConcurrentHashMap
import javax.websocket.OnOpen
import javax.websocket.Session
import javax.websocket.OnClose
import javax.websocket.OnMessage
import javax.websocket.server.PathParam
import kotlin.collections.LinkedHashMap
import kotlin.math.log


@ServerEndpoint("/ws/Connect/{userId}")
@Component
@Slf4j
class WebSocketSever {
    object Clients{
        val clients = LinkedHashMap<Int, Session>()
    }
    @Autowired
    private lateinit var userRepository: UserRepository
    private val messageQueue = HashMap<Int, Queue<WarningMessage>>()
    @OnOpen
    fun onOpen(session: Session, @PathParam("userId") param:Int){
        val user:Int =param
        Clients.clients[user] = session
        println("用户${param}连接成功")

        if(messageQueue[user]!=null){
                messageQueue[user]?.forEach{
                session.asyncRemote.sendText(it.toString())
            }
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