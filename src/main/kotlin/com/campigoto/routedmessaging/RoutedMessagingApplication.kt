package com.campigoto.routedmessaging

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.jms.annotation.EnableJms

@EnableJms
@SpringBootApplication
class RoutedMessagingApplication

fun main(args: Array<String>) {
    runApplication<RoutedMessagingApplication>(*args)
}
