# Tommy's Mood Relay - CQRS and Event-Driven Architecture Demo

## Run the Project

```bash
cd Lea_und_Max
docker compose up --build
```

Wait for all services to start.

## Access the Applications

- **Mood Sender**: http://localhost:5001
- **Mood Wall**: http://localhost:5002
- **RabbitMQ Management**: http://localhost:15672 (username: `guest`, password: `guest`)

## How to Use

1. Open Mood Sender (http://localhost:5001) in one browser window
2. Open Mood Wall (http://localhost:5002) in another browser window
3. Click on images on the Mood Sender page
4. Watch statistics update in real-time on the Mood Wall page (auto-refreshes every 2 seconds)