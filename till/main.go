package main

import (
	"fmt"

	"CQRS_EDA_TILL/consumer"
	"CQRS_EDA_TILL/producer"
)

func main() {
	kassen_mitarbeiter, err := producer.NewKassenpersonal()
	if err != nil {
		fmt.Printf("Fehler beim erstellen des Kassenpersonals: %v", err)
	}

	_, err = consumer.NewBarista()
	if err != nil {
		fmt.Printf("Fehler beim erstellen des Barista: %v", err)
	}

	for {
		fmt.Println("Bitte gebe deine Bestellung auf!")
		fmt.Println("1) Cappuchino")
		fmt.Println("2) Flat White")
		fmt.Println("3) Espresso")
		fmt.Println("4) Americano")
		var order string
		fmt.Scan(&order)
		fmt.Printf("Bestellung: %v erhalten!", order)
		go kassen_mitarbeiter.CreateOrder(order)
	}
}
