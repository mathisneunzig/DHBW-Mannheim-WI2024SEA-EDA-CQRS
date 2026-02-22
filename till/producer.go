package main

import (
	"encoding/json"
	"fmt"
	"time"

	"CQRS_EDA_TILL/broker"
	"CQRS_EDA_TILL/consumer_read"
	"CQRS_EDA_TILL/consumer_write"

	"github.com/google/uuid"
)

const ORDER_CREATED string = "order"

func printOrderTerminal() (string, string, string) {
	menu := map[string]string{
		"1": "Cappuchinno",
		"2": "Flat White",
		"3": "Espresso",
		"4": "Americano",
	}

	var drink, size, name string

	fmt.Println("\n--- Neue Bestellung ---")
	fmt.Println("1) Cappuchino\n2) Flat White\n3) Espresso\n4) Americano")
	fmt.Print("Eingabe: ")
	fmt.Scan(&drink)
	drinkName, ok := menu[drink]
	if !ok {
		fmt.Println("Ungültige Wahl, wähle Standard: Espresso")
		drinkName = "Espresso"
	}
	fmt.Print("Größe (S/M/L): ")
	fmt.Scan(&size)
	fmt.Print("Dein Name: ")
	fmt.Scan(&name)
	fmt.Printf("Bestellung: %v - %v fur %v erhalten!\n", drinkName, size, name)
	return drinkName, size, name
}

func main() {
	go consumer_write.SendToWork()
	go consumer_read.SendToWork()
	time.Sleep(time.Second)

	brokerUtil, err := broker.NewBrokerUtil("tcp://localhost:1883", "producer", "user", "password")
	if err != nil {
		fmt.Println(err)
		return
	}

	drink, size, name := printOrderTerminal()

	orderCreationEvent := broker.CoffeeOrderCreated{
		OrderID: uuid.NewString(),
		Drink:   drink,
		Size:    size,
		Name:    name,
	}

	payload, err := json.Marshal(orderCreationEvent)
	if err != nil {
		fmt.Println(err)
		return
	}

	brokerUtil.PublishEvent(ORDER_CREATED, string(payload))

	select {}
}
