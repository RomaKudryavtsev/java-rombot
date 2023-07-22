# java-rombot
Reactive WhatsApp message sending tool

### Description

rombot allows to send template messages via WhatsApp Business / Cloud API.

### Swagger UI

![](rombot_ui.png)

### Requirements
- WhatsApp Business Account
- Application in Meta for Developers
- Template message created
- JDK / JVM installed
- MongoDB with contacts configured

### Before start

Add your token and phone ID (issued by Meta) to application.properties.

### Tech stack
- Java 17
- Spring WebFlux + Project Reactor
- Reactive MongoDB
- WebClient