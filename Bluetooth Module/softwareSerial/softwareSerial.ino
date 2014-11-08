#include <SoftwareSerial.h>

SoftwareSerial mySerial(3,2);

void setup() {
Serial.begin(9600);
mySerial.begin(9600);
delay(500);
Serial.println("go");
}

void loop() {
  int data;
  if(mySerial.available()){
    data = mySerial.read();
  delay(20);
  Serial.println(data);
  } 
}
m
