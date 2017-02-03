#include <Servo.h>

Servo c1,c2,c3,c4,c5,c6,c7;

void setup() {
  Serial.begin(9600);
  c1.write(0);
  c2.write(0);
  c3.write(0);
  c4.write(0);
  c5.write(0);
  c6.write(0);
  c7.write(0);
}

void loop() {
  if (Serial.available() > 0) {
    String incoming = Serial.readStringUntil('\n');
    translateString(data);
  }
}

void translateString(String data){
    String stickName = incoming.substring(0,incoming.indexOf(' '));
    int power = (incoming.substring(incoming.indexOf(' '))).toInt();
    switch (stickName){
      case "c1":
        c1.write(power);
      case "c2":
        c2.write(power);   
      case "c3":
        c3.write(power);   
      case "c4":
        c4.write(power);   
      case "c5":
        c5.write(power);   
      case "c6":
        c6.write(power);   
      case "c7":
        c7.write(power);   
    }
}

