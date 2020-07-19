package com.campigoto.routedmessaging.consumer

import com.campigoto.routedmessaging.JMS_SELECTOR
import com.campigoto.routedmessaging.MESSAGE_QUEUE
import com.campigoto.routedmessaging.MESSAGE_TO_ALL_QUEUE
import org.springframework.core.env.Environment
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class JmsConsumer {

    @Inject
    lateinit var env: Environment

    @JmsListener(destination = MESSAGE_QUEUE, containerFactory = "filteredContainerFactory")
    fun listenMessage(message: String?) {
        val selector = env.getProperty(JMS_SELECTOR)
        println("$message to $selector")
    }

    @JmsListener(destination = MESSAGE_TO_ALL_QUEUE, containerFactory = "containerFactory")
    fun listenMessageToAll(message: String?) = println("$message to all")
}