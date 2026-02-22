package main

import (
	"context"
	"encoding/json"
	"flag"
	"log"
	"net/http"
	"time"

	"github.com/VictorHackerVH/DHBW-Mannheim-WI2024SEA-EDA-CQRS/shared"
	amqp "github.com/rabbitmq/amqp091-go"
)

func errorHelper(err error, msg string) {
	if err != nil {
		log.Panicf("%s: %s", msg, err)
	}
}

func main() {
	flag.Parse()

	conn, err := amqp.Dial("amqp://guest:guest@localhost:5672/")
	errorHelper(err, "Failed to connect to RabbitMQ")
	defer conn.Close()

	ch, err := conn.Channel()
	errorHelper(err, "Failed to open channel")
	defer ch.Close()

	q, err := ch.QueueDeclare(
		"hello",
		false,
		false,
		false,
		false,
		nil,
	)
	errorHelper(err, "Failed to declare queue")

	http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
		http.ServeFile(w, r, "index.html")
	})

	http.HandleFunc("/trigger", func(w http.ResponseWriter, r *http.Request) {
		sensorID := r.URL.Query().Get("sensor")

		event := shared.SensorEvent{
			Sensor:    shared.SensorID(sensorID),
			Timestamp: time.Now(),
		}

		body, _ := json.Marshal(event)

		ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
		defer cancel()

		err = ch.PublishWithContext(ctx,
			"",
			q.Name,
			false,
			false,
			amqp.Publishing{
				ContentType: "application/json",
				Body:        body,
			})

		if err != nil {
			log.Printf("Fehler beim Senden: %s", err)
			w.WriteHeader(http.StatusInternalServerError)
			return
		}

		log.Printf("Event via Web-UI gesendet: %s", sensorID)
		w.WriteHeader(http.StatusOK)
	})

	log.Fatal(http.ListenAndServe(":8081", nil))
}
