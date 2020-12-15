#include <AFMotor.h>

#define right A1
#define left A0
#define trig 0
#define echo 1

AF_DCMotor leftFront(1, MOTOR12_1KHZ); 
AF_DCMotor rightFront(2, MOTOR12_1KHZ);
AF_DCMotor rightRear(3, MOTOR12_1KHZ);
AF_DCMotor leftRear(4, MOTOR12_1KHZ);

long duration;
int distance;

void setup() {
  pinMode(left,INPUT);
  pinMode(right,INPUT); 
  pinMode(trig,OUTPUT);
  pinMode(echo,INPUT); 
}

void loop(){
  
  distance = measureDistance();

  if(distance < 15){
    stopAction();
  }
  else{
    if(digitalRead(left)==0 && digitalRead(right)==0){
      forwardAction();
    }
  
    else if(digitalRead(left)==0 && !digitalRead(right)==0){
      leftTurn();
    }
  
    else if(!digitalRead(left)==0 && digitalRead(right)==0){
      rightTurn();
    }
  
    else if(!digitalRead(left)==0 && !digitalRead(right)==0){
      stopAction();
    }
  }
}

void forwardAction() {
  leftFront.run(FORWARD);
  leftFront.setSpeed(100);
  rightFront.run(FORWARD);
  rightFront.setSpeed(100);
  rightRear.run(FORWARD);
  rightRear.setSpeed(100);
  leftRear.run(FORWARD);
  leftRear.setSpeed(100);
}

void leftTurn() {
  leftFront.run(BACKWARD);
  leftFront.setSpeed(150);
  rightFront.run(FORWARD);
  rightFront.setSpeed(100);
  rightRear.run(FORWARD);
  rightRear.setSpeed(100);
  leftRear.run(BACKWARD);
  leftRear.setSpeed(150);
}

void rightTurn() {
  leftFront.run(FORWARD);
  leftFront.setSpeed(100);
  rightFront.run(BACKWARD);
  rightFront.setSpeed(150);
  rightRear.run(BACKWARD);
  rightRear.setSpeed(150);
  leftRear.run(FORWARD);
  leftRear.setSpeed(100);
}

void stopAction() {
  leftFront.run(RELEASE);
  leftFront.setSpeed(0);
  rightFront.run(RELEASE);
  rightFront.setSpeed(0);
  rightRear.run(RELEASE);
  rightRear.setSpeed(0);
  leftRear.run(RELEASE);
  leftRear.setSpeed(0);
}

int measureDistance(){
  digitalWrite(trig, LOW);
  delayMicroseconds(2);
  digitalWrite(trig, HIGH);
  delayMicroseconds(10);
  digitalWrite(trig, LOW);
  
  duration = pulseIn(echo, HIGH);

  distance = duration * 0.034 / 2;

  return distance;
}
