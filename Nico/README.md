# ARK Tek Defense System 🦖

Dieses Projekt simuliert ein Verteidigungssystem aus dem Spiel *ARK: Survival Ascended*. 

## Wie funktioniert das System?
- **Live API:** Beim Start zieht sich das System Dinos über die offizielle ARK-API.
- **Publisher/Subscriber:** Wenn man im Dashboard einen Radar-Ping simuliert, berechnet das Backend die Gefahr. Ist der Dino gefährlich (Threat-Level 5 oder höher), schickt der Publisher ein Event an RabbitMQ.
- **Entkoppelte Subscriber:** Zwei völlig unabhängige Systeme (das Web-Dashboard und die Geschütztürme) haben diesen Channel abonniert, empfangen das Event und reagieren darauf.

## Tech Stack
- Java & Spring Boot
- RabbitMQ 
- Docker & Docker Compose
- HTML/CSS/JS (Vanilla) für das Live-Dashboard

## Starten des Projekts

Das System ist komplett in Docker verpackt. 

1. Öffne ein Terminal in diesem Verzeichnis (wo die `docker-compose.yml` liegt).
2. Starte die Container mit :
   docker-compose up -d --build