package com.campigoto.routedmessaging.consumer

import com.campigoto.routedmessaging.JMS_SELECTOR
import org.springframework.core.env.Environment
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class JmsConsumer {

    @Inject
    lateinit var env: Environment

    @JmsListener(destination = "message", containerFactory = "containerFactory")
    fun listenMessage(message: String?) {
        val selector = env.getProperty(JMS_SELECTOR)
        println("$message to $selector")
    }
}