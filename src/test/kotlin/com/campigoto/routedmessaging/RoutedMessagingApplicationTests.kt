package com.campigoto.routedmessaging

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jms.core.JmsTemplate
import javax.inject.Inject

@SpringBootTest
class RoutedMessagingApplicationTests {

    @Inject
    lateinit var jmsTemplate: JmsTemplate

    @Test
    fun contextLoads() {
        assertNotNull(jmsTemplate)
    }

}
