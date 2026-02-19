package consumer

import (
	"encoding/json"
	"fmt"
	"time"

	"CQRS_EDA_TILL/broker"

	mqtt "github.com/eclipse/paho.mqtt.golang"
)

type Barista struct {
	broker *broker.BrokerUtil
}

func NewBarista() (*Barista, error) {
	util, err := broker.NewBrokerUtil("tcp://localhost:1883", "consumer", "user", "password")
	if err != nil {
		fmt.Printf("Error: %v", err)
		return nil, err
	}

	b := &Barista{
		broker: util,
	}

	b.broker.SubscribeTopic("order/new", b.onOrderCreated)

	return b, nil
}

func (b *Barista) onOrderCreated(_ mqtt.Client, msg mqtt.Message) {
	var coffe_order_created_event broker.CoffeeOrderCreated
	err := json.Unmarshal(msg.Payload(), &coffe_order_created_event)
	if err != nil {
		fmt.Printf("Fehler beim lesen der Bestellung: %v", err)
		return
	}

	fmt.Printf("(Barista) Bestellung %v erhalten. Beginne mit der Zubereitung!", coffe_order_created_event.OrderID)

	time.Sleep(time.Second * 5)

	order_finished_event := broker.CoffeeOrderFinished{
		OrderID:  coffe_order_created_event.OrderID,
		Finished: true,
	}
	payload, err := json.Marshal(order_finished_event)
	if err != nil {
		fmt.Printf("Fehler beim Rückmelden des Status: %v", err)
		return
	}

	b.broker.PublishMessage("orders/status", string(payload))
}
