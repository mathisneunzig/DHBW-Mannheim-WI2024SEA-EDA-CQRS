package broker

import (
	"fmt"

	mqtt "github.com/eclipse/paho.mqtt.golang"
)

type CoffeeOrderCreated struct {
	OrderID string `json:"order_id"`
	Name    string `json:"name"`
	Drink   string `json:"drink"`
	Size    string `json:"size"`
}

type CoffeeOrderFinished struct {
	OrderID  int  `json:"order_id"`
	Finished bool `json:"finished"`
}

type BrokerUtil struct {
	mqttClient mqtt.Client
}

func NewBrokerUtil(url string, clientID string, user string, password string) (*BrokerUtil, error) {
	opts := mqtt.NewClientOptions()
	opts.AddBroker(url)
	opts.SetClientID(clientID)
	opts.SetUsername(user)
	opts.SetPassword(password)

	client := mqtt.NewClient(opts)
	token := client.Connect()
	if token.Wait() && token.Error() != nil {
		return nil, token.Error()
	}

	return &BrokerUtil{mqttClient: client}, nil
}

func (b *BrokerUtil) SubscribeTopic(topic string, message_callback mqtt.MessageHandler, name string) {
	token := b.mqttClient.Subscribe(topic, 1, nil)
	token.Wait()
	if token.Error() != nil {
		fmt.Printf("Fehler! Topic %s nicht subscribed! Fehler: %s", topic, token.Error())
	}
	fmt.Printf("Erfolgreich %s subscribed! von %v", topic, name)
}

func (b *BrokerUtil) PublishEvent(topic string, msg string) {
	token := b.mqttClient.Publish(topic, 1, false, msg)
	token.Wait()
	if token.Error() != nil {
		fmt.Printf("Fehler beim veröffentlichen: %s", token.Error())
	}
}
