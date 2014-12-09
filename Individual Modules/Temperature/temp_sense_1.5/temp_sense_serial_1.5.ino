#include <SoftwareSerial.h>

SoftwareSerial mySerial(10, 11); // RX, TX

//TMP36 Pin Variables
int sensorPin = 2; //Says we are working with a sensor in analog pin 0
/*
* setup() - this function runs once when you turn your Arduino on
 * We initialize the serial connection with the computer
 */


void setup()
{
  Serial.begin(9600); //Start the serial connection with the computer
  mySerial.begin(9600);
  mySerial.println("Hello, world?");
  pinMode(sensorPin, INPUT);
  //to view the result open the serial monitor 
}
void loop() // run over and over againx
{
  //getting the voltage reading from the temperature sensor
  int reading = analogRead(sensorPin); 
  // converting that reading to voltage, for 3.3v arduino use 3.3
  float voltage = reading * 5.0;
  voltage /= 1024.0; // print out the voltage to the serial port

  double temperature = log(((10240000/reading) - 10000));
  temperature = 1 / (0.001129148 + (0.000234125 + (0.0000000876741 * temperature * temperature ))* temperature );
  temperature = temperature - 273.15;

  double farenheit = temperature * 9.0 / 5.0 + 32.0;
  delay(1000); //waiting a second

  if (mySerial.available()){
    mySerial.print(temperature);
    mySerial.print(" C / ");
    mySerial.print(farenheit);
    mySerial.print(" F / ");
    mySerial.print(reading);
    mySerial.println(" ADC");
  }
  if(Serial.available()){
    Serial.print(temperature);
    Serial.print(" C / ");
    Serial.print(farenheit);
    Serial.print(" F / ");
    Serial.print(reading);
    Serial.println(" ADC");
  }

}




