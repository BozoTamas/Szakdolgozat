#include <AFMotor.h> //A shield könyvtára
#include <Servo.h>

AF_DCMotor rightFront(1, MOTOR12_1KHZ); //M1 port
AF_DCMotor leftFront(2, MOTOR12_1KHZ); //M2 port
AF_DCMotor leftRear(3, MOTOR12_1KHZ); //M3 port
AF_DCMotor rightRear(4, MOTOR12_1KHZ); //M4 port

Servo myservo;

void setup() {
  myservo.attach(10);
}

void loop() {
  forwardAction(100);
}