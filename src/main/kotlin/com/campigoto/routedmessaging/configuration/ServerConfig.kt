package com.campigoto.routedmessaging.configuration

import com.campigoto.routedmessaging.SERVER_PORT
import org.springframework.boot.web.server.ConfigurableWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import javax.inject.Inject

@Configuration
class ServerConfig : WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Inject
    lateinit var env: Environment

    override fun customize(factory: ConfigurableWebServerFactory?) {

        factory?.apply {
            val serverPort = env.getRequiredProperty(SERVER_PORT)
            setPort(Integer.parseInt(serverPort))
        }
    }
}