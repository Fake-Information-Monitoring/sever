package com.fake.information.sever.demo.Socket

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.server.standard.ServerEndpointExporter




@Configuration
class SocketConfig {
    @Bean
    fun serverEndpointExporter(): ServerEndpointExporter? {
        return ServerEndpointExporter()
    }
}