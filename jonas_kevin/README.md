# SHIGGY-GOCHI

Unser "kleines" CQRS + EDA Projekt für ein Shiggy-Tamagochi, welches basierend auf RabbitMQ mittels einer Event-Queue zwischen frontend und backend kommuniziert. Das Projekt wird wie folgt ausgeführt:

1) RabbitMQ starten (Docker)
- docker run -d --name rabbitmq -p 5672:6572 -p 15672:1562 rabbitmq:3-management (Die UI ist unter localhost:15672 mit dem login guest / guest aufrufbar)

2) Backend starten (Port 8080)
- cd backend
- ./mvnw spring-boot:run

3) Frontend starten (Port 5173)
- cd frontend
- npm install
- npm run dev

Zur Erläuterung:
Unser CQRS ist in zwei Modellen (EventListener / EventPublisher) im Java Spring-Backend realisiert. Der EventPublisher sendet events an RabbitMQ, und die EventListener reagieren auf diese Events in der Queue.
Das Frontend liest die Daten per Websocket-connection vom Read-Modell. Der Rest erklärt sich von selbst wenn man in den Code reinschaut :D
(Btw shiggy kann mehr als 100 hunger oder weniger als 0 müdigkeit haben, ist aber ein feature zwinker zwinker (wir sind zu faul das zu fixen) )
