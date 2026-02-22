package consumer_write

import (
	"encoding/json"
	"fmt"

	"CQRS_EDA_TILL/broker"

	mqtt "github.com/eclipse/paho.mqtt.golang"
)

type Barista struct {
	broker *broker.BrokerUtil
	orders []broker.CoffeeOrderCreated
}

func NewBarista() (*Barista, error) {
	util, err := broker.NewBrokerUtil("tcp://localhost:1883", "barista", "user", "password")
	if err != nil {
		fmt.Printf("Error: %v", err)
		return nil, err
	}

	b := &Barista{
		broker: util,
		orders: []broker.CoffeeOrderCreated{},
	}

	b.broker.SubscribeTopic("order", b.onOrderCreated, "barista")

	return b, nil
}

func (b *Barista) onOrderCreated(_ mqtt.Client, msg mqtt.Message) {
	fmt.Println("Nachricht eingegangen")
	var coffe_order_created_event broker.CoffeeOrderCreated
	err := json.Unmarshal(msg.Payload(), &coffe_order_created_event)
	if err != nil {
		fmt.Printf("Fehler beim lesen der Bestellung: %v", err)
		return
	}
	fmt.Printf("(Barista) Bestellung %v erhalten. Beginne mit der Zubereitung!\n", coffe_order_created_event.OrderID)
}

func SendToWork() {
	_, err := NewBarista()
	if err != nil {
		fmt.Println(err)
		return
	}
	select {}
}
