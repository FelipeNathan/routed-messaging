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
import org.springframework.jms.support.converter.MessageType
import javax.inject.Inject

@Configuration
class JmsConfig {

    @Inject
    lateinit var env: Environment

    fun jmsFactory() = ActiveMQConnectionFactory(env.getProperty(BROKER_URL) ?: "tcp://localhost:61616")

    @Bean(name = ["filteredContainerFactory"])
    fun filteredContainerFactory(): DefaultJmsListenerContainerFactory {

        val selector = env.getRequiredProperty(JMS_SELECTOR)
        println("$JMS_SELECTOR: $selector")

        return SelectorJmsListenerContainerFactory(selector).also {
            it.setConnectionFactory(jmsFactory())
        }
    }

    @Bean(name = ["containerFactory"])
    fun containerFactory() = DefaultJmsListenerContainerFactory().also {
        it.setConnectionFactory(jmsFactory())
    }

    @Bean
    fun jacksonJmsMessageConverter() = MappingJackson2MessageConverter().apply {
        setTargetType(MessageType.TEXT)
        setTypeIdPropertyName("_type")
    }

    inner class SelectorJmsListenerContainerFactory(private val selector: String?) : DefaultJmsListenerContainerFactory() {

        override fun createListenerContainer(endpoint: JmsListenerEndpoint): DefaultMessageListenerContainer {
            return super.createListenerContainer(endpoint).also {
                it.messageSelector = this.selector
            }
        }
    }
}