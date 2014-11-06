// Connect a sensor or potentiometer to analog pin A0.
// The onboard LED at D13 is toggled everytime a sensor value is requested from the Arduino.
const int SENS_PIN = A0;
const int LED_PIN = 13;

int sensorValue = 0;

boolean toggle = true;

void setup() {
    Serial.begin(9600);
}

void loop() {
    if (Serial.available()) {
        Serial.read();
        sensorValue = analogRead(SENS_PIN);
        Serial.println(sensorValue);
        digitalWrite(LED_PIN, toggle); // toggle the LED
        toggle = !toggle;
    }
}
