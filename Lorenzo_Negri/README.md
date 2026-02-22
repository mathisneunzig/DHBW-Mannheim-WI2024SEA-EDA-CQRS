# Bestellverwaltung - EDA + CQRS Pattern

## Projektübersicht

Name: Lorenzo Negri 
Matrikelnummer: 5082963

Dieses Projekt implementiert ein Bestellverwaltungssystem unter Verwendung des CQRS (Command Query Responsibility Segregation)-Patterns in Kombination mit EDA (Event-Driven Architecture). Die Anwendung ermöglicht das Anlegen und Anzeigen von Bestellungen über eine Website.

### Aufbau

 **Frontend:** HTML/CSS/Javascript 
 **Backend:** Java Spring Boot 
            **Order Service:** Erstellt Bestellungen (Port 8080)
            **Projection Service** Liest/zeigt Bestellungen (Port 8083)
            **RabbitMQ** Message Broker (Port 5672, Der Port für das UI: 15672)

### Event-Kommunikation

Der Order Service veröffentlicht ein "OrderPlacedEvent" (siehe common/OrderPlacedEvent.java) an eine RabbitMQ-Exchange.
Der Projection Service konsumiert dieses Event und speichert die Daten in seiner eigenen H2-Datenbank.

### Technische Anforderungen

- **Backend**: Java Spring Boot
- **Message Broker**: RabbitMQ
- **Datenbank**: H2 (In-Memory)
- **Frontend**: Vanilla HTML/CSS/JavaScript

### Voraussetzungen

- Java 17+
- Docker Desktop

### Schritte

1. **RabbitMQ starten**
   ```bash
   docker compose up -d
   ```
2. **(optional) Prüfung**
   ```bash
   docker ps 
   ```

3. **Backend-Services starten**
   Zuerst das Common-Projekt bauen:
   ```bash
   cd backend
   ./mvnw install -pl common
   ```

   Dann beide Services starten:
   ```bash
   # Order Service 
   cd order-service
   ./mvnw spring-boot:run

   # (optional) ./mvnw clean spring-boot:run

   # Projection Service (neues Terminal)
   cd Lorenzo_Negri/backend/projection-service
   ./mvnw spring-boot:run

   # (optional) ./mvnw clean spring-boot:run
   ```

4. **Frontend starten**
   # Live Server Extension in VS Code herunterladen und dann mit Rechtsklick auf die index.html Datei mittels Live Server die Website öffnen

### Bei Problemen: 

   ```bash 
   # Ports checken 
   netstat -ano | findstr :8080
   netstat -ano | findstr :8083

   # Falls Port belegt: 
   cmd 

   taskkill /F /PID <"PID-Number">

   # Docker Container beenden 
   docker compose down 
   # Neustarten 
   docker compose up -d 

   ```

### Ports:

- **RabbitMQ Management UI**: http://localhost:15672 (Name: guest ,  Passwort: guest)
- **Order Service**: http://localhost:8080
- **Projection Service**: http://localhost:8083
