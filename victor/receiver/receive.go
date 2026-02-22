package main

import (
	"encoding/json"
	"log"
	"net/http"
	"sync"

	"github.com/VictorHackerVH/DHBW-Mannheim-WI2024SEA-EDA-CQRS/shared"
	amqp "github.com/rabbitmq/amqp091-go"
)

type HomeState struct {
	sync.RWMutex
	Lamps map[string]bool `json:"lamps"`
}

var currentState = &HomeState{
	Lamps: map[string]bool{
		"lampe_flur":   false,
		"lampe_kueche": false,
		"lampe_bad":    false,
	},
}

func errorHelper(err error, msg string) {
	if err != nil {
		log.Panicf("%s: %s", msg, err)
	}
}

func handleSensorEvent(body []byte) {
	var event shared.SensorEvent
	if err := json.Unmarshal(body, &event); err != nil {
		log.Printf(err.Error())
		return
	}
	currentState.Lock()
	defer currentState.Unlock()

	switch event.Sensor {
	case shared.SensorFlur:
		currentState.Lamps["lampe_flur"] = true
	case shared.SensorKueche:
		currentState.Lamps["lampe_kueche"] = true
	case shared.SensorBad:
		currentState.Lamps["lampe_bad"] = true
	default:
		log.Printf("unbekannter sensor")
	}
}

func main() {
	conn, err := amqp.Dial("amqp://guest:guest@localhost:5672/")
	errorHelper(err, "Failed to connect to RabbitMQ")
	defer conn.Close()

	ch, err := conn.Channel()
	errorHelper(err, "Failed to open a channel")
	defer ch.Close()

	q, err := ch.QueueDeclare(
		"hello",
		false,
		false,
		false,
		false,
		nil,
	)
	errorHelper(err, "Failed to declare a queue")

	msgs, err := ch.Consume(
		q.Name,
		"",
		true,
		false,
		false,
		false,
		nil,
	)
	errorHelper(err, "Failed to register a consumer")

	go func() {
		for d := range msgs {
			handleSensorEvent(d.Body)
		}
	}()

	log.Printf(" [*] Waiting for messages. To exit press CTRL+C")

	http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
		http.ServeFile(w, r, "index.html")
	})
	http.HandleFunc("/state", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "application/json")
		currentState.RLock()
		defer currentState.RUnlock()
		json.NewEncoder(w).Encode(currentState.Lamps)
	})

	err = http.ListenAndServe(":8080", nil)
}
