package brokerUtil

import (
	"fmt"

	mqtt "github.com/eclipse/paho.mqtt.golang"
)

type brokerUtil struct {
	mqttClient mqtt.Client
}

func NewBrokerUtil(url string, clientID string, user string, password string) (*brokerUtil, error) {
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

	return &brokerUtil{mqttClient: client}, nil
}

func (b *brokerUtil) SubscribeTopic(topic string, message_callback mqtt.MessageHandler) {
	token := b.mqttClient.Subscribe(topic, 1, nil)
	token.Wait()
	if token.Error() != nil {
		fmt.Printf("Fehler! Topic %s nicht subscribed! Fehler: %s", topic, token.Error())
	}
	fmt.Printf("Erfolgreich %s subscribed!", topic)
}

func (b *brokerUtil) PublishMessage(topic string, msg string) {
	token := b.mqttClient.Publish(topic, 1, false, msg)
	token.Wait()
	if token.Error() != nil {
		fmt.Printf("Fehler beim veröffentlichen: %s", token.Error())
	}
}
