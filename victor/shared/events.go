package shared

import "time"

type SensorID string

const (
	SensorFlur   SensorID = "sensor-flur"
	SensorKueche SensorID = "sensor-kueche"
	SensorBad    SensorID = "sensor-bad"
)

type SensorEvent struct {
	Sensor    SensorID  `json:"sensor_id"`
	Timestamp time.Time `json:"timestamp"`
}
