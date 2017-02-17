#include <TimerOne.h>

void setup() {
  Timer1.initialize(20000);
  Serial.begin(9600);
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
    power = power/2;
    if(data.indexOf("c1")!=-1){
      Timer1.pwm(3, 51+power, 20000);
      Serial.println(stickName + " " + power);
    }
    else if(data.indexOf("c2")!=-1){
      Timer1.pwm(5, 51+power, 20000);
      Serial.println(stickName + " " + power);
    }
    else if(data.indexOf("c3")!=-1){
      Timer1.pwm(6, 51+power, 20000);
      Serial.println(stickName + " " + power);
    }
    else if(data.indexOf("c4")!=-1){
      Timer1.pwm(9, 51+power, 20000);
      Serial.println(stickName + " " + power);
    }
    else if(data.indexOf("c5")!=-1){
      Timer1.pwm(10, 51+power, 20000);
      Serial.println(stickName + " " + power);
    }
    else if(data.indexOf("c6")!=-1){
      Timer1.pwm(11, 51+power, 20000);
      Serial.println(stickName + " " + power);
    }
}

