# Routed Messaging 
![Test Coverage](https://github.com/FelipeNathan/Routed-Messaging/workflows/Test%20Coverage/badge.svg?branch=master)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=FelipeNathan_Routed-Messaging&metric=alert_status)](https://sonarcloud.io/dashboard?id=FelipeNathan_Routed-Messaging)

Routing a message, means that you can create differents targets for the same queue. The routed-messaging is a sample for how to solve one problem - Load Balancer - when the message needs to reach to a specifc server using ActiveMQ as a message broker.

## Requirement
 * ActiveMQ
 * Java 1.8+
 
## Build
* `mvn clean install`

## Environment Variables
* spring.activemq.broker-url (opcional, default: tcp://localhost:61616)
  * URL to connect to ActiveMQ
  
* server.port (required, for configure 2 or more servers in same machine for test)
  * The server port (localhost:8080, localhost:8081)
  
* jms.selector (required)
  * That's is the filter, the message selector, who'll say "I'll pick just the message which have this selector configured"

## Routes
In this example, I configured three endpoints:

* The default endpoint: localhost:{server.port}
  * This endpoint create a message with a default value `1` in the property called `server`, with this, when run the server with `-Djms.selector=server=1` this server will pick this message to process.
* The endpoint with an id: localhost:{server.port}/{id}
  * This one will create a message with `{id}` value, with this, when run the server with `-Djms.selector=server={id}` this server will pick this message to process.

## Running
* This application send a message with a integer property with name `server` (server=1)
* Run an ActiveMQ server
* Create 2 or more instances of this applications (with differents jms.selector and server.port values):
  * `java -jar "-Dserver.port=8080" "-Djms.selector=server=1" .\routed-messaging-0.0.1-SNAPSHOT.jar`
  * `java -jar "-Dserver.port=8081" "-Djms.selector=server=2" .\routed-messaging-0.0.1-SNAPSHOT.jar`
* Run the command `curl http://localhost:8080/` (or use your preferred tool for http request)
  * A mensage Hello World will be delivered to an instance of server=1
  ![Message to server=1 without param](/images/sent-server-1.png?raw=true)
* Run the command `curl http://localhost:8080/2` (or use your preferred tool for http request)
  * A mensage Hello World will be delivered to an instance of server=2
  ![Message to server=2](/images/sent-server-2.png?raw=true)
* Run the command `curl http://localhost:8080/1` (or use your preferred tool for http request)
  * A mensage Hello World will be delivered to an instance of server=1
  ![Message to server=1 with param](/images/sent-server-1-with-param.png?raw=true)
* Run the command `curl http://localhost:8080/2` twice (or use your preferred tool for http request)
  * A mensage Hello World will be delivered to an instance of server=2 twice
  ![Message to server=2](/images/sent-server-2-twice-in-a-row.png?raw=true)

Try to send multiple message to the same server id, you'll see the message being delivered to the same server always.

## How it works?
A load balancer manage which server it'll deliver the http request, the server that receives the request will put a message to message broker with the param id, and the server that is configured with a selector with these id will pick the message...

This application doesn't have a load balancer, but notice that I've always send a http request to one instance (with 8080 port), never used the 8081 port and even this, the server with `server=2` (port 8081) could pick a message

Note: Message with no id configured will be listened for all instances that are listening the same queue, try to send a message to `localhost:8080/all` 2 or more times, the servers will intercalate who'll read the message
