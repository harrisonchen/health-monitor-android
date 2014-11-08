/* This is a simple test for two way traffic via bluetooth 
   but you can try it first using the USB cable to the serial 
   monitor without the bluetooth module connected.
   
   Note that some bluetooth modules come set to a different baud rate.
   This means you need to alter the command
                Serial.begin(9600) accordingly
   Same goes for the setting in the bottom right corner on the 
   serial monitor */             

byte byteRead;

void setup()
{
    Serial.begin(9600);
    Serial.println("OK then, you first, say something.....");
    Serial.println("Go on, type something in the space above and hit Send,");
    Serial.println("or just hit the Enter key, then I will repeat it!");
    Serial.println("");
}

void loop() {
   //  check if data has been sent from the computer: 
  if (Serial.available()) {
    /* read the most recent byte */
    byteRead = Serial.read();
    /*ECHO the value that was read, back to the serial port. */
    Serial.write(byteRead);
  }
}

