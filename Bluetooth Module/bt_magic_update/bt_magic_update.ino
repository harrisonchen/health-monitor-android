#include <SoftwareSerial.h>
//YOU NEED TO SET THE PINS HERE!
#define Ihavereadthisandmadecahngesandaddedslachestothisline 1
SoftwareSerial Serialsoft2(3,2); // RX, TX


//"this code automates the programing of the bt jy-mcu module
//" designed by james villeneuve for reprap.org
//THIS VERSION USES SOFT SERIAL AND WORKS ON MISO PORT

int inline_questioning =0;// know what question at
int inline_questioning_wait=0; // waiting for something
long device_speed ;//detect baud and store it
long final_baud;//stores final_baud rate
void  setup(){
  Serial.begin(57600);// this is default baud rate
  
  
startlogo();//calls logo
if (Ihavereadthisandmadecahngesandaddedslachestothisline == 1 ){
  
  Serial.println("");
  Serial.println("");
  Serial.println("");
  Serial.println("");
  Serial.println("");
  Serial.println("");
  Serial.println("");
  Serial.println("");
  Serial.println("");
  Serial.println("error!");
  Serial.println("you need to set the pins for i/o. read the first 10 lines in the compiler. thanks!");
  Serial.println("defaults set rx pin (10) and tx pin 11");
int loopforever = 0;
while( loopforever == 0){
  loopforever = loopforever ;
  


}
  
}

}


void bauddetect( long x){//this solved baudrate
Serial.print("baudrate:");
Serial.print(x);
int byte1;
int detect=0;//used to know if detected
long time_keeper=millis();//get start millseconds
Serialsoft2.begin(x);//starts at baudrate
delay(400);//stabilizer
Serialsoft2.print("AT");//initialization command
while(millis()-time_keeper<2000){
  //while within 
   if (Serialsoft2.available() > 0) { 
    byte1= Serialsoft2.read();
  if (byte1==79 | byte1==75){
    Serial.print(".");
    detect++; //if o or k is sent then increment
    byte1=0; //clear
  }
 
 
    };// conditions met for char sent
 }//while loop
 Serialsoft2.flush();
 Serialsoft2.end();//stops serial at baud rate
 delay(200);//stabilizer
 if (detect < 2) {x=0; Serial.println("_no");}//clears if result is not a valid responce from bt host
 else{ Serial.println("_yes");// if baud then 
 final_baud=x;//store baud rate for use.
 }
}

void loop(){
  
  if (Ihavereadthisandmadecahngesandaddedslachestothisline != 1) { //prevents run until user remarks out comments to ensure pins have been changed
  //this is where loop stuff happens
  int byte;//byte used to store serial data from host machine
 if (Serial.available() > 0) {byte= Serial.read();};//byte stored 
 
if  (inline_questioning ==0 & inline_questioning_wait==0) {//first question responce has not been asked yet
Serial.println("                      "); //prevents false display when not ready
Serial.println("if you are ready to program BT controller, then Type in [y] or [Y]");
Serial.println("to abort or restart, simply exit serial monitor tool for arduino ide");
inline_questioning_wait= 1; // now it has been asked and we are waiting for an an answer back
}
if  (inline_questioning ==0 & inline_questioning_wait==1) {
  //ok we wait for a y or Y
  if ( byte ==89 ) {inline_questioning = 1;byte=0;inline_questioning==1; inline_questioning_wait=0;};
  if ( byte ==121 ) {inline_questioning = 1;byte=0;inline_questioning==1; inline_questioning_wait=0;};
};

if  (inline_questioning ==1 & inline_questioning_wait==0) {//first question responce has not been asked yet
Serial.println(""); // some space to know i is a different question
Serial.println("");
Serial.println("");
Serial.println("");
Serial.println("here is the ramps 1.4 controller pinout");
board();
Serial.println("");
Serial.println("CONNECT IN THE FOLLOWING ORDER.....");
Serial.println("CONNECT GND OF BT CARD TO GND OF RAMPS");
Serial.println("CONNECT VCC OF BT CARD TO 5V OF RAMPS");
Serial.println("CONNECT RX OF BT CARD TO PIN 16(TX) OF RAMPS THE FIRSTHEST CONNECTION ON THE EDGE OF CARD");
Serial.println("CONNECT TX OF BT CARD TO PIN 17(RX) OF RAMPS THE SECOND PIN IN FROM THE EDGE OF CARD");
Serial.println("");
Serial.println("WHEN THIS IS COMPLETE IF THE LIGH ON BT IS BLINKING THIS MEANS IT IS READY TO PROGRAM");
Serial.println("IF BT MODULE IS PLUGGED IN AS DESCRIBED ABOVE AND YOU ARE READY TO CONTINUE PRESS [Y] OR [y]");
inline_questioning_wait=1; // now it has been asked and we are waiting for an an answer back
}
if  (inline_questioning ==1 & inline_questioning_wait==1) {
  //ok we wait for a y or Y
  if ( byte ==89 )  {inline_questioning = 2 ;byte=0; inline_questioning_wait=0;};
  if ( byte ==121 ) {inline_questioning = 2 ;byte=0; inline_questioning_wait=0;};
};
if  (inline_questioning ==2 & inline_questioning_wait==0) {//first question responce has not been asked yet
Serial.println(""); // some space to know i is a different question
Serial.println("detecting device....");

inline_questioning_wait=1; // now it has been asked and we are waiting for an an answer back
device_speed=1200 ;//baud 1
bauddetect(device_speed);//returns with device or zero
device_speed=2400 ;//baud 1
bauddetect(device_speed);//returns with device or zero
device_speed=4800 ;//baud 1
bauddetect(device_speed);//returns with device or zero
device_speed=9600 ;//baud 1
bauddetect(device_speed);//returns with device or zero
device_speed=19200 ;//baud 1
bauddetect(device_speed);//returns with device or zero
device_speed=38400 ;//baud 1
bauddetect(device_speed);//returns with device or zero
device_speed=57600 ;//baud 1
bauddetect(device_speed);//returns with device or zero
device_speed=115200 ;//baud 1
bauddetect(device_speed);//returns with device or zero
device_speed=230400 ;//baud 1
bauddetect(device_speed);//returns with device or zero
device_speed=460800 ;//baud 1
bauddetect(device_speed);//returns with device or zero
device_speed=921600 ;//baud 1
bauddetect(device_speed);//returns with device or zero
device_speed=11382400 ;//baud 1
bauddetect(device_speed);//returns with device or zero

if (final_baud>0){Serial.println("Device speed detected.");
inline_questioning =3; 
inline_questioning_wait=0;
}
else {Serial.println("Device error! reset and try again!");
for (int wait=0; wait > 0;){}//loop forever until reset!
}


} //input
if  (inline_questioning ==3 & inline_questioning_wait==0) {//first question responce has not been asked yet
Serial.println(""); // some space to know i is a different question
Serial.println("Device info:");
deviceinfo();//calls and prints device info

Serial.println("-------Commands:--------------");
Serial.println("1........setbaudrate");
Serial.println("2.....renamedeviceid");
Serial.println("3.........Change pin");
Serial.println("- choose above options 1,2,3 -");
inline_questioning_wait=1; //question asked
};

if (inline_questioning ==3 & inline_questioning_wait==1) {
  //ok we wait for a y or Y
  if ( byte ==49 )  {setbaundrate();};
  if ( byte ==50 ) {renamedevice();};
  if ( byte ==51 ) {changepin();};
};
  };//end of restriction if met runs loop
};// void loop
void board(){
  
    
Serial.println("  current code is for serial2 to be softserial. you choose which pins to use in file defaults are pin 10,11 rx,txs rx and to have bt jy-mcu adapter. this includes hc03,hc05,hc06 bt");
Serial.println(" power the bt chip with 5v (warning if not on a board and powering directly you need to lower chip voltage to 3.3v. if bt adaptor is on a board you are ok as it has voltage shifter installed");
Serial.println("power bt by isp header. connect gnd of bt to gnd on isp header. same with vcc. be sure to watch orientation of pin 1 on header. if all goes well bt should be blinking.");
Serial.println("miso        1[]   []  vcc 5v");
Serial.println("sck          []   []  mosi");
Serial.println("reset        []   []  GND");


}

void startlogo(){
                                                                                                                                                  
Serial.println("   SOFTWARE SERIAL BLUETOOTH MAGIC. THE MAGICS THE SAME, EVEN IF YOU DON'T HAVE THE SERIAL HARDWARE PORT!");


  
  
  
  
}


void deviceinfo(){
Serialsoft2.begin(final_baud);//now that baud rate is known
delay(200);//stablizer
Serialsoft2.flush();
Serial.print("baudrate:");
Serial.println(final_baud); 
Serial.print("BT Module:");
long time_keeper=millis();//get start millseconds
Serialsoft2.print("AT+VERSION");//initialization command
while(millis()-time_keeper<2000){
if (Serialsoft2.available()>0){Serial.print(char(Serialsoft2.read()));}

}//end of while
Serial.println("");
Serial.println("");
Serial.println("password:");
Serial.println("idname:");
}

void setbaundrate(){
Serial.println("");
Serial.println("SET BAUD RATE (speed of communication for BT) generally 115200");
Serial.println("---------------choose number to set speed-------------------- ");
Serial.println("[1]... 1200 baud");
Serial.println("[2]... 2400 baud");
Serial.println("[3]... 4800 baud");
Serial.println("[4]... 9600 baud");
Serial.println("[5]... 19200 baud");
Serial.println("[6]... 38400 baud");
Serial.println("[7]... 57600 baud");
Serial.println("[8]... 115200 baud");
Serial.println("[9]... 230400 baud");
Serial.println("[A]... 460800 baud");
Serial.println("[B]... 921600 baud");
Serial.println("[C]... 1382400 baud");
Serial.println("[X]... exit menu");
delay(400);
Serial.flush();
  int byte3=10 ;//byte used to store serial data from host machine
  while( byte3 !=0){//loop until a value chosen
 if (Serial.available() > 0) {byte3= Serial.read();};//byte stored 
  //looping till char pressed
  if  (byte3==49){Serialsoft2.print("AT+BAUD1");final_baud=1200;byte3=0;}
  if  (byte3==50){Serialsoft2.print("AT+BAUD2");final_baud=2400;byte3=0;}
  if  (byte3==51){Serialsoft2.print("AT+BAUD3");final_baud=4800;byte3=0;}
  if  (byte3==52){Serialsoft2.print("AT+BAUD4");final_baud=9600;byte3=0;}
  if  (byte3==53){Serialsoft2.print("AT+BAUD5");final_baud=19200;byte3=0;}
  if  (byte3==54){Serialsoft2.print("AT+BAUD6");final_baud=38400;byte3=0;}
  if  (byte3==55){Serialsoft2.print("AT+BAUD7");final_baud=57600;byte3=0;}
  if  (byte3==56){Serialsoft2.print("AT+BAUD8");final_baud=115200;byte3=0;}
  if  (byte3==57){Serialsoft2.print("AT+BAUD9");final_baud=230400;byte3=0;}
  if  (byte3==65){Serialsoft2.print("AT+BAUDA");final_baud=460800;byte3=0;}
  if  (byte3==97){Serialsoft2.print("AT+BAUDA");final_baud=460800;byte3=0;}
  if  (byte3==66){Serialsoft2.print("AT+BAUDB");final_baud=921600;byte3=0;}
  if  (byte3==98){Serialsoft2.print("AT+BAUDB");final_baud=921600;byte3=0;}
  if  (byte3==67){Serialsoft2.print("AT+BAUDC");final_baud=1382400;byte3=0;}
  if  (byte3==99){Serialsoft2.print("AT+BAUDC");final_baud=1382400;byte3=0;}
  if  (byte3==88){inline_questioning =3;  inline_questioning_wait==0; byte3=0; exit;}
  if  (byte3==120){inline_questioning =3;  inline_questioning_wait==0;byte3=0;exit;}
  }
  
delay(2000);
Serialsoft2.flush();
delay(1000);//restarting comm
 Serialsoft2.end();// stop com and wait. will restart
  inline_questioning =2;
  inline_questioning_wait=0;
  //;final_baud=1200; Serial
}
void renamedevice(){
Serial.println("");
Serial.println("THIS IS TO RENAME DEVICE. AFTER THIS TYPE IN UP TO 20 CHAR AND CLICK SEND TO COMMITIT. THIS IS DEVICE NAME OVER BT. after send wait 5 seconds ");
Serial.flush(); //CLEARS
delay(1500);//restarting comm
Serial.flush(); //CLEARS
 int temp4=0;//used to count char
 char str[20];//20 char limit
//this is where wait is done for text
while ( !Serial.available()){ //loop until serial available
}//end of while loop
long temp5=millis();//get time
while (millis()-temp5<4000 ||temp4== 20){// ok now we had 4 seconds to get data or when 20 char recieved
  if (Serial.available()){
    str[temp4]=Serial.read(); //read byte
    temp4 ++;
  }//serial available
}//end of while loop
// ok here is were we send serial data to bt device

Serialsoft2.print("AT+NAME");//SENDS COMMAND
for (int i= 0; i<temp4; i++){ 
Serialsoft2.print(str[i]); //variable 
}//end for

Serial.println(" ");
Serial.println("device  setup complete. ");

delay(2000);
Serialsoft2.flush();
delay(1000);//restarting comm
 Serialsoft2.end();// stop com and wait. will restart
  inline_questioning =2;
  inline_questioning_wait=0;
}//end rename


void changepin(){
Serial.println("");
Serial.println("THIS IS TO set 4 pin password. if it errors out you will need to rerun. also rerun if you want to change it ");
Serial.flush(); //CLEARS
delay(1000);//restarting comm
Serial.flush(); //CLEARS
 int temp4=0;//used to count char
 char str[4];//4 char limit
//this is where wait is done for text
while ( !Serial.available()){ //loop until serial available
}//end of while loop
long temp5=millis();//get time
while (millis()-temp5<4000 ||temp4== 3){// ok now we had 4 seconds to get data or when 20 char recieved
  if (Serial.available()){
    str[temp4]=Serial.read(); //read byte
    temp4 ++;
  }//serial available
}//end of while loop
// ok here is were we send serial data to bt device
if (temp4>2){ //0-3 is 4 char
  {for (int j=0; j<temp4; j++){//2
  
  if (str[j] < 48 || str[j]>57){//3 if char less that 0 or greater than 9. prevens alpa char
  Serial.println("data can no be set because it contains alpha characters. 0-9 only and 4 digits! temporaraly setting password to 0000");
  str[1]='0';
  str[2]='0';
  str[3]='0';
  str[4]='0';
  temp4=4;//set number of char
  delay(200);
  }//if //3
};//for//2
Serial.print("AT+PIN");//SENDS COMMAND
Serialsoft2.print("AT+PIN");//SENDS COMMAND
for (int i= 0; i<temp4; i++){ //4
Serial.print(str[i]); //variable 
Serialsoft2.print(str[i]); //variable 
};//for//4
//if

}//1
}//5 

else { Serial.print("error. data needs to be 4 characters.");}
Serial.println(" ");
Serial.println("device 4 digit passcode routine complete.");

delay(2000);
Serialsoft2.flush();
delay(1000);//restarting comm
 Serialsoft2.end();// stop com and wait. will restart
  inline_questioning =2;
  inline_questioning_wait=0;


}


