# Tommy's Mood Relay - CQRS and Event-Driven Architecture Demo

A distributed system demonstrating Command Query Responsibility Segregation (CQRS) and event-driven microservice communication using Docker and RabbitMQ.

## Overview

Mood Relay consists of three containerized services that communicate asynchronously via RabbitMQ:

1. **mood_sender** - Producer service on port 5001
2. **rabbitmq** - Message broker on ports 5672 and 15672
3. **mood_wall** - Consumer service on port 5002

## Concept

- **Website A (Mood Sender)**: Users click on images (happy or sad) to send mood events
- **RabbitMQ**: Routes mood events asynchronously between services
- **Website B (Mood Wall)**: Displays real-time statistics:
  - Total happy count
  - Total sad count
  - Mood score (happy - sad)

No direct communication between websites occurs - all data flows through RabbitMQ.

## CQRS Architecture

This application implements the CQRS pattern:

**Command Side (Write Model)**:
- `mood_sender` handles Commands (sending mood events)
- Publishes events to RabbitMQ
- Does not query data

**Query Side (Read Model)**:
- `mood_wall` handles Queries (retrieving statistics)
- Consumes events from RabbitMQ
- Maintains its own denormalized read model
- Provides optimized data access via `/stats` endpoint

```
Commands (Write)                                    Queries (Read)
┌─────────────────┐         ┌──────────────┐         ┌─────────────────┐
│  Mood Sender    │         │   RabbitMQ   │         │   Mood Wall     │
│  (Command)      │────────▶│   (Events)   │────────▶│   (Query)       │
│  Port: 5001     │  Events │  Port: 5672  │  Events │  Port: 5002     │
│                 │         │              │         │  Read Model     │
└─────────────────┘         └──────────────┘         └─────────────────┘
```

## Message Format

Messages are JSON with the following structure:

```json
{
  "type": "good" | "bad",
  "timestamp": "2026-02-16T16:15:00.000Z"
}
```

Queue name: `mood_events`

## Quick Start

### Prerequisites

- Docker
- Docker Compose

### Run the Project

```bash
cd Rax-Mommel
docker compose up --build
```

Wait for all services to start (about 30 seconds).

### Access the Applications

- **Mood Sender**: http://localhost:5001
- **Mood Wall**: http://localhost:5002
- **RabbitMQ Management**: http://localhost:15672 (username: `guest`, password: `guest`)

## How to Use

1. Open Mood Sender (http://localhost:5001) in one browser window
2. Open Mood Wall (http://localhost:5002) in another browser window
3. Click on images on the Mood Sender page
4. Watch statistics update in real-time on the Mood Wall page (auto-refreshes every 2 seconds)

## Project Structure

```
Rax-Mommel/
├── docker-compose.yml
├── README.md
├── mood_sender/
│   ├── Dockerfile
│   ├── requirements.txt
│   ├── app.py
│   ├── static/
│   └── templates/
└── mood_wall/
    ├── Dockerfile
    ├── requirements.txt
    ├── app.py
    ├── static/
    └── templates/
```
