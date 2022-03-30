#include "DHT.h"

#define DHTPIN 2     // Digital pin connected to the DHT sensor

#define DHTTYPE DHT11   // DHT 11
DHT dht(DHTPIN, DHTTYPE);

void setup() {
  Serial.begin(9600);
  pinMode(3,OUTPUT);
  pinMode(4,OUTPUT);
  digitalWrite(3,HIGH);
  digitalWrite(4,LOW);
  dht.begin();
}

void loop() {
  // Wait a few seconds between measurements.
  delay(1000);
  float t = dht.readTemperature();
  if(!isnan(t)) {
    Serial.print(t);
    Serial.print("Z");
  }
}
