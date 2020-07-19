package com.campigoto.routedmessaging.configuration

import com.campigoto.routedmessaging.BROKER_URL
import com.campigoto.routedmessaging.JMS_SELECTOR
import org.apache.activemq.ActiveMQConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.config.JmsListenerEndpoint
import org.springframework.jms.listener.DefaultMessageListenerContainer
import org.springframework.jms.support.converter.MappingJackson2MessageConverter
import org.springframework.jms.support.converter.MessageConverter
import org.springframework.jms.support.converter.MessageType
import javax.inject.Inject

@Configuration
class JmsConfig {

    @Inject
    lateinit var env: Environment

    @Bean
    fun jmsFactory(): ActiveMQConnectionFactory {

        val brokerUrl = env.getProperty(BROKER_URL)
        return ActiveMQConnectionFactory(brokerUrl ?: "tcp://localhost:61616")
    }

    @Bean
    fun containerFactory(): DefaultJmsListenerContainerFactory {

        val selector = env.getRequiredProperty(JMS_SELECTOR)
        println("$JMS_SELECTOR: $selector")
        val containerFactory = SelectorJmsListenerContainerFactory(selector)
        containerFactory.setConnectionFactory(jmsFactory())

        return containerFactory
    }

    @Bean
    fun jacksonJmsMessageConverter(): MessageConverter? {

        val converter = MappingJackson2MessageConverter()
        converter.setTargetType(MessageType.TEXT)
        converter.setTypeIdPropertyName("_type")
        return converter
    }

    inner class SelectorJmsListenerContainerFactory(private val selector: String?) : DefaultJmsListenerContainerFactory() {

        override fun createListenerContainer(endpoint: JmsListenerEndpoint): DefaultMessageListenerContainer {
            return super.createListenerContainer(endpoint).also {
                it.messageSelector = this.selector
            }
        }
    }
}