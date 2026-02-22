# Projekt: EDA mit CQRS - Smart Home

## RabbitMQ starten

podman-compose up -d

## Sender und Receiver starten

go run receiver/receive.go
Receiver erreichbar unter: http://localhost:8080

go run sender/send.go
Sender erreichbar unter: http://localhost:8081
