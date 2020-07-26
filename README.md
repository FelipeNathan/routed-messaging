# Routed Messaging 
![Test Coverage](https://github.com/FelipeNathan/Routed-Messaging/workflows/Test%20Coverage/badge.svg?branch=master)

O roteamento de mensagem pode abordar diferentes cenários, desde consumir a mensagem em métodos com diferentes configurações de prioridades até ser consumido por servidor específico (cenário criado nesta aplicação) 

## Requisitos
 * ActiveMQ
 * Java 1.8+
 
## _Build_
* `mvn clean package`

## Variáveis de ambiente
* spring.activemq.broker-url (opcional, default: tcp://localhost:61616)
  * Url de conexão com o ActiveMQ
  
* server.port
  * Porta utilizada na aplicação
  
* jms.selector
  * Filtro utilizado pela aplicação, para validar qual será a regra de consumo de mensagem (esta é a chave do negócio)

## Rotas
* localhost:{server.port}
  * Envia uma mensagem Hello World para server=1 (default) 
* localhost:{server.port}/{id}
  * Envia uma mensagem Hello World para server={id}
  
obs: Pode enviar a mensagem de qualquer servidor, pois vai mandar pra mesma fila, o que determina quem vai consumir é o JMS Selector (server=1)

## Testando o cenário
* Esta aplicação envia a mensagem com propriedade do tipo `int` com nome `server` (server=1)
* Suba um servidor ActiveMQ
* Crie 2+ instâncias da aplicação (detalhe no jms.selector, server=1 e server=2):
  * `java -jar "-Dserver.port=8080" "-Djms.selector=server=1" .\routed-messaging-0.0.1-SNAPSHOT.jar`
  * `java -jar "-Dserver.port=8081" "-Djms.selector=server=2" .\routed-messaging-0.0.1-SNAPSHOT.jar`
* Execute o comando `curl http://localhost:8080/`
  * A mensagem Hello World será entregue a instância do server=1
  ![Message to server=1 without param](/images/sent-server-1.png?raw=true)
* Execute o comando `curl http://localhost:8080/2`
  * A mensagem Hello World será entregue a instância do server=2
  ![Message to server=2](/images/sent-server-2.png?raw=true)
* Execute o comando `curl http://localhost:8080/1`
  * A mensagem Hello World será entregue a instância do server=1
  ![Message to server=1 with param](/images/sent-server-1-with-param.png?raw=true)
* Execute o comando `curl http://localhost:8080/2` 2 vezes seguidas
  * A mensagem Hello World será entregue a instância do server=2 em ambas as chamadas
  ![Message to server=2](/images/sent-server-2-twice-in-a-row.png?raw=true)
