package main

import (
	"fmt"
	"time"

	brokerUtil "CQRS_EDA_TILL"

	mqtt "github.com/eclipse/paho.mqtt.golang"
)

func main() {
	broker, err := brokerUtil.NewBrokerUtil("tcp://localhost:1883", "producer", "user", "password")
	if err != nil {
		fmt.Printf("Error: %v", err)
		return
	}

	message_callback := func(_ mqtt.Client, msg mqtt.Message) {
		fmt.Printf("Neues Event erhalten: %v", string(msg.Payload()))
	}
	broker.SubscribeTopic("test/topic", message_callback)

	for {
		time.Sleep(1 * time.Second)
	}
}
