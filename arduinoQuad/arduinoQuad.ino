#include <Servo.h>

Servo c1,c2,c3,c4,c5,c6;

void setup() {
  c1.attach(3);
  c2.attach(5);
  c3.attach(6);
  c4.attach(9);
  c5.attach(10);
  c6.attach(11);
  Serial.begin(9600);
  c1.write(0);
  c2.write(0);
  c3.write(0);
  c4.write(0);
  c5.write(0);
  c6.write(0);
}

void loop() {
  if (Serial.available() > 0) {
    String data = Serial.readStringUntil('\n');
    translateString(data);
  }
}

void translateString(String data){
    data.replace(" ", "");
    String stickName = data.substring(0,data.indexOf(','));
    int power = (data.substring(data.indexOf(',')+1)).toInt();
    
    if(data.indexOf("c1")!=-1){
      c1.write(power);
      Serial.println(stickName + " " + power);
    }
    else if(data.indexOf("c2")!=-1){
      c2.write(power);
      Serial.println(stickName + " " + power);
    }
    else if(data.indexOf("c3")!=-1){
      c3.write(power);
      Serial.println(stickName + " " + power);
    }
    else if(data.indexOf("c4")!=-1){
      c4.write(power);
      Serial.println(stickName + " " + power);
    }
    else if(data.indexOf("c5")!=-1){
      c5.write(power);
      Serial.println(stickName + " " + power);
    }
    else if(data.indexOf("c6")!=-1){
      c6.write(power);
      Serial.println(stickName + " " + power);
    }
}

