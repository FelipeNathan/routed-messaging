package com.campigoto.routedmessaging.controller

import com.campigoto.routedmessaging.MESSAGE_QUEUE
import com.campigoto.routedmessaging.MESSAGE_TO_ALL_QUEUE
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.jms.core.JmsTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.inject.Inject

@RestController
@RequestMapping("/")
class HomeController {

    @Inject
    lateinit var jmsTemplate: JmsTemplate

    @GetMapping
    fun home(): ResponseEntity<String> {

        sendMessage()
        return ResponseEntity(MESSAGE_SENT, HttpStatus.OK)
    }

    @GetMapping("{id}")
    fun home(@PathVariable id: Int): ResponseEntity<String> {

        sendMessage(id)
        return ResponseEntity(MESSAGE_SENT, HttpStatus.OK)
    }

    @GetMapping("/all")
    fun sentToAll(): ResponseEntity<String> {
        jmsTemplate.convertAndSend(MESSAGE_TO_ALL_QUEUE, "This is a message")
        return ResponseEntity(MESSAGE_SENT, HttpStatus.OK)
    }

    private fun sendMessage(id: Int = 1) {
        jmsTemplate.convertAndSend(MESSAGE_QUEUE, "Hello World") { postProcessor ->
            postProcessor.apply {
                setIntProperty("server", id)
            }
        }
    }

    companion object {
        const val MESSAGE_SENT = "Message sent"
    }
}