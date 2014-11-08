#include <SoftwareSerial.h>

SoftwareSerial mySerial(1,0);

void setup() {
Serial.begin(9600);
mySerial.begin(9600);
delay(5000);
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

