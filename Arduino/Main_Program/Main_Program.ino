//Szükséges könyvtárok meghivatkozása
#include <AFMotor.h>
#include <Servo.h>
#include <SoftwareSerial.h>

//Definiáljuk a szenzorok tüskéit
#define rightIRSensor A1
#define leftIRSensor A0
#define trigPin 0
#define echoPin 1

//A villanymotorok shield-en való helyének inicializálása
AF_DCMotor rightFront(1, MOTOR12_1KHZ); //M1 port
AF_DCMotor leftFront(2, MOTOR12_1KHZ); //M2 port
AF_DCMotor leftRear(3, MOTOR12_1KHZ); //M3 port
AF_DCMotor rightRear(4, MOTOR12_1KHZ); //M4 port

Servo myservo; //Szervo motor példányosítása

SoftwareSerial myBluetooth(2,13);

long duration, distance, leftObstacle, rightObstacle, obstacleDistance; //Távolság meghatározásához szükséges változók

long int data, newData; //A bluetooth modulról beolvasott adatok

String logFile = "";

bool obstacleDetected, lineNotFound; //Akadály kikerülésekor használt logikai változók

void setup() {

  myBluetooth.begin(9600);
  
  myservo.attach(9); //Lefoglaljuk a szervo-nak a Digitális 9-es portot

  //Beállítjuk, hogy a szenzorok tüskéi be- vagy kimenetek
  pinMode(leftIRSensor,INPUT);
  pinMode(rightIRSensor,INPUT); 
  pinMode(trigPin,OUTPUT);
  pinMode(echoPin,INPUT);

  myservo.write(90);

  logFile = "> A robot elindult.";
}

void loop(){
  
  while(myBluetooth.available() == 0);

  data = myBluetooth.parseInt();

  if(data == 222){
    logEvent("_Automata követés elkezdődött! ");
    do{
      
      distance = measureDistance();

      if(newData != 'E'){
        if(distance < 10){
          logEvent("_Akadály érzékelve! ");
          stopAction();
          delay(100);
          ObstacleAvoidance();
        }
        else{
          if(digitalRead(leftIRSensor)==0 && digitalRead(rightIRSensor)==0){
            forwardAction(100);
            newData = myBluetooth.read();
          }
  
          else if(digitalRead(leftIRSensor)==0 && !digitalRead(rightIRSensor)==0){
            rightAction(100,130);
            newData = myBluetooth.read();
          }
  
          else if(!digitalRead(leftIRSensor)==0 && digitalRead(rightIRSensor)==0){
            leftAction(100,130);
            newData = myBluetooth.read();
          }
  
          else if(!digitalRead(leftIRSensor)==0 && !digitalRead(rightIRSensor)==0){
            stopAction();
            newData = myBluetooth.read();
          }
        }
      }
      else{
        logEvent("_Követés szüneteltetve! ");
        do{
          stopAction();       
          newData = myBluetooth.read();
        }while(newData != 'S' && newData != 'B');
        if(newData == 'S'){
          logEvent("_Követés újraindítva! ");
        }
      }
    }while(newData != 'B');
    logEvent("_Követés felfüggesztve! ");
    stopAction();
  }
  else if(data == 111){
    logEvent("_Irányítás elkezdődött! ");
    do{
      data = myBluetooth.read();

      switch(data){
        case 'F':
          forwardAction(150);
          break;
        case 'L':
          leftAction(200,200);
          break;          
        case 'R':
          rightAction(200,200);
          break;
        case 'B':
          backwardAction(150);
          break;
        case 'S':
          stopAction();
          break;
        case 'T':
          testAction();
          stopAction();
          break;
        default:
          break;
      }
    }while(data != 'E');
    logEvent("_Irányítás felfüggesztve! ");
  }
  else if(data = 333){
    do{
      data = myBluetooth.read();
      switch(data){
        case 'G':
          myBluetooth.println(logFile);
          break;
        case 'D':
          logFile = "";
          break;          
        default:
          break;
      }
    }while(data != 'B');
  }
}


//Az UH szenzor működéséhez szükséges metódus
long measureDistance(){
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
  
  duration = pulseIn(echoPin, HIGH);

  distance = duration * 0.034 / 2;

  return distance;
}

//Pásztázás elvégzése, hogy el tudjuk dönteni merre kelljen kikerülni az akadályt
void ObstacleAvoidance(){ 
  //Az akadály érzékelése után egy kicsit hátrál a robot
  backwardAction(110);
  delay(250);
  stopAction();
  delay(100);
  
  //Megvizsgáljuk, hogy a jobb oladlon van-e akadály
  myservo.write(1);
  delay(400);
  rightObstacle = measureDistance();
  delay(200);

  //Megvizsgáljuk a bal oldalt is
  myservo.write(179);
  delay(400);
  leftObstacle = measureDistance();
  delay(200);

  //A szervót visszaállítjuk középállásba
  myservo.write(85);
  delay(500);
    
  if(leftObstacle < rightObstacle){ //Ha a bal oldalon található tárgy van közelebb
    logEvent("_Akadály kikerülése jobbra! ");
    avoidObstacleToRight();
  }
  else{ //Ha a jobb oldalon található tárgy van közelebb
    logEvent("_Akadály kikerülése balra! ");
    avoidObstacleToLeft();
  } 
}

//Mindandigg előrehalad a robot amíg érzékeli az akadályt
void followObstacle(){
  obstacleDetected = true;
  obstacleDistance = measureDistance();
  do{
    forwardAction(110);
    if(measureDistance() <= obstacleDistance + 5 ){ //Mindaddig haladunk előre, amíg az akadály +- 5cm-re található.
      obstacleDetected = true;
    }
    else{
      obstacleDetected = false;
    }
  }while(obstacleDetected);
  obstacleDistance=0; //Kinullázzuk az értéket a hibalehetőségek csökkentésére
}

//Megkeressük az akadály szélét
void reFindObstacle(){
  obstacleDetected = false;
  do{
    forwardAction(110);
    obstacleDistance = measureDistance();
    delay(100);
    if(obstacleDistance < 40){ //Az akadályt megtaláltnak vesszük abban az esetben, ha a robottól kevesebb, mint 40cm-re található
      obstacleDetected = true;
    }
    else{
      obstacleDetected = false;
    }
  }while(!obstacleDetected);
  obstacleDistance = 0;
}

void avoidObstacleToRight(){   //Akadály kikerülése jobbra
  rightAction(200,200);
  delay(600);
  stopAction();
  delay(50);
  myservo.write(180);
  followObstacle();
  forwardAction(110);
  delay(650);
  leftAction(200,200);
  delay(550);
  reFindObstacle();
  followObstacle();
  forwardAction(110);
  delay(200);
  leftAction(200,200);
  delay(300);
  myservo.write(90);
  delay(50);
  reFindLine();
}

void avoidObstacleToLeft(){ //Akadály kikerülése balra
  leftAction(200,200);
  delay(600);
  stopAction();
  delay(50);
  myservo.write(0);
  followObstacle();
  forwardAction(110);
  delay(650);
  rightAction(200,200);
  delay(550);
  reFindObstacle();
  followObstacle();
  forwardAction(110);
  delay(200);
  rightAction(200,200);
  delay(300);
  myservo.write(90);
  delay(50);
  reFindLine();
}


void reFindLine(){
  lineNotFound = true;
  do{
    forwardAction(110);
    if(digitalRead(leftIRSensor)==0 && digitalRead(rightIRSensor)==0){
      lineNotFound = true;
    }
    else if(digitalRead(leftIRSensor)==0 && !digitalRead(rightIRSensor)==0){
      leftAction(110,130);
      delay(300);
      lineNotFound = false;
    }
    else if(!digitalRead(leftIRSensor)==0 && digitalRead(rightIRSensor)==0){
      rightAction(110,130);
      delay(300);
      lineNotFound = false;
    }
  }while(lineNotFound);
  logEvent("_Akadály kikerülve! ");
}

//Motorok vezérlése
//Előrehaladás
void forwardAction(int sebesseg) {
  leftFront.run(FORWARD);
  leftFront.setSpeed(sebesseg);
  rightFront.run(FORWARD);
  rightFront.setSpeed(sebesseg);
  rightRear.run(FORWARD);
  rightRear.setSpeed(sebesseg);
  leftRear.run(FORWARD);
  leftRear.setSpeed(sebesseg);
}
void backwardAction(int sebesseg){
  leftFront.run(BACKWARD);
  leftFront.setSpeed(sebesseg);
  rightFront.run(BACKWARD);
  rightFront.setSpeed(sebesseg);
  rightRear.run(BACKWARD);
  rightRear.setSpeed(sebesseg);
  leftRear.run(BACKWARD);
  leftRear.setSpeed(sebesseg);
}
//Balkanyar
void leftAction(int eloreSebesseg, int hatraSebesseg) {
  leftFront.run(BACKWARD);
  leftFront.setSpeed(hatraSebesseg);
  rightFront.run(FORWARD);
  rightFront.setSpeed(eloreSebesseg);
  rightRear.run(FORWARD);
  rightRear.setSpeed(eloreSebesseg);
  leftRear.run(BACKWARD);
  leftRear.setSpeed(hatraSebesseg);
}
//Jobbkanyar
void rightAction(int eloreSebesseg, int hatraSebesseg) {
  leftFront.run(FORWARD);
  leftFront.setSpeed(eloreSebesseg);
  rightFront.run(BACKWARD);
  rightFront.setSpeed(hatraSebesseg);
  rightRear.run(BACKWARD);
  rightRear.setSpeed(hatraSebesseg);
  leftRear.run(FORWARD);
  leftRear.setSpeed(eloreSebesseg);
}
//Motorok leállítása
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

void testAction(){
  for(int i = 0; i <= 180; i++){
    myservo.write(i);
    delay(10);
  }
  myservo.write(90);
  
  leftAction(150,150);
  delay(500);
  rightAction(150,150);
  delay(500);
  forwardAction(200);
  delay(200);
  backwardAction(200);
  delay(200);
  logEvent("_A teszt befejeződött!");
}

void logEvent(String event){ //A naplózás elvégzéséhez használt metódus
  logFile += event;
}
