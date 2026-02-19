package producer

import (
	"fmt"

	"CQRS_EDA_TILL/broker"

	mqtt "github.com/eclipse/paho.mqtt.golang"
)

type Kassenpersonal struct {
	broker *broker.BrokerUtil
}

func NewKassenpersonal() (*Kassenpersonal, error) {
	broker, err := broker.NewBrokerUtil("tcp://localhost:1883", "producer", "user", "password")
	if err != nil {
		fmt.Printf("Error: %v", err)
		return nil, err
	}
	return &Kassenpersonal{
		broker: broker,
	}, nil
}

func (k *Kassenpersonal) message_callback(_ mqtt.Client, msg mqtt.Message) {
	fmt.Printf("Neues Event erhalten: %v", string(msg.Payload()))
}

func (k *Kassenpersonal) CreateOrder(order string) {
	k.broker.PublishMessage("order/new", order)
}
