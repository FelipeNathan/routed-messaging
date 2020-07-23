package com.campigoto.routedmessaging.consumer

import com.campigoto.routedmessaging.JMS_SELECTOR
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment
import org.springframework.jms.core.JmsTemplate
import javax.inject.Inject

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class JmsConsumerTest {

    @Inject
    lateinit var jmsTemplate: JmsTemplate

    @Inject
    lateinit var env: Environment

    @BeforeEach
    fun config() {
        jmsTemplate.receiveTimeout = JMS_TIMEOUTg
    }

    @Test
    fun shouldReadMessageQueueServer1() {
        sendMessage(1)
        assertEquals(HELLO_WORLD, jmsTemplate.receiveSelectedAndConvert(MESSAGE_TEST_QUEUE, env.getRequiredProperty(JMS_SELECTOR)))
    }

    @Test
    fun messageShouldBeNullWhenServerNot1() {
        sendMessage(2)
        sendMessage(3)
        assertNull(jmsTemplate.receiveSelectedAndConvert(MESSAGE_TEST_QUEUE, env.getRequiredProperty(JMS_SELECTOR)))
    }

    @Test
    fun shouldReadMessageToAllQueue() {
        jmsTemplate.convertAndSend(MESSAGE_TO_ALL_TEST_QUEUE, HELLO_WORLD)
        assertEquals(HELLO_WORLD, jmsTemplate.receiveAndConvert(MESSAGE_TO_ALL_TEST_QUEUE))
    }

    private fun sendMessage(id: Int = 1) {
        jmsTemplate.convertAndSend(MESSAGE_TEST_QUEUE, HELLO_WORLD) { postProcessor ->
            postProcessor.apply {
                setIntProperty(SERVER_PROPERTY, id)
            }
        }
    }

    companion object {
        const val HELLO_WORLD = "Hello World"
        const val SERVER_PROPERTY = "server"
        const val MESSAGE_TEST_QUEUE = "message.test"
        const val MESSAGE_TO_ALL_TEST_QUEUE = "message_to_all.test"
        const val JMS_TIMEOUT = 2000L
    }
}