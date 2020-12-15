#define rightIR A1
#define leftIR A0
#define rightFrontGreen 4
#define rightFrontRed 5
#define leftFrontGreen 6
#define leftFrontRed 7
#define leftRearGreen 8
#define leftRearRed 9
#define rightRearGreen 10
#define rightRearRed 11
#define trig 2
#define echo 3

long duration;
int distance;

void setup() {
  pinMode(rightIR, INPUT);
  pinMode(leftIR, INPUT);
  pinMode(rightFrontGreen, OUTPUT);
  pinMode(rightFrontRed, OUTPUT);
  pinMode(leftFrontGreen, OUTPUT);
  pinMode(leftFrontRed, OUTPUT);
  pinMode(leftRearGreen, OUTPUT);
  pinMode(leftRearRed, OUTPUT);
  pinMode(rightRearGreen, OUTPUT);
  pinMode(rightRearRed, OUTPUT);
  pinMode(trig, OUTPUT);
  pinMode(echo, INPUT);

}

void loop() {

  distance = measureDistance();
  
  if(distance < 15){
    stopAll();
  }
  else{
    if(digitalRead(rightIR)==0 && digitalRead(leftIR)==0){
      stopAll();
    }
    else if(digitalRead(rightIR)==0 && !digitalRead(leftIR)==0){
      rightTurn();
    }
    else if(!digitalRead(rightIR)==0 && digitalRead(leftIR)==0){
      leftTurn();  
    }
    else if(!digitalRead(rightIR)==0 && !digitalRead(leftIR)==0){
      forward();
    }
  }
}

void stopAll(){
  digitalWrite(rightFrontGreen, LOW);
  digitalWrite(rightFrontRed, HIGH);
  digitalWrite(leftFrontGreen, LOW);
  digitalWrite(leftFrontRed, HIGH);
  digitalWrite(leftRearGreen, LOW);
  digitalWrite(leftRearRed, HIGH);
  digitalWrite(rightRearGreen, LOW);
  digitalWrite(rightRearRed, HIGH);
}

void forward(){
  digitalWrite(rightFrontGreen, HIGH);
  digitalWrite(rightFrontRed, LOW);
  digitalWrite(leftFrontGreen, HIGH);
  digitalWrite(leftFrontRed, LOW);
  digitalWrite(leftRearGreen, HIGH);
  digitalWrite(leftRearRed, LOW);
  digitalWrite(rightRearGreen, HIGH);
  digitalWrite(rightRearRed, LOW);
}

void leftTurn(){
  digitalWrite(rightFrontGreen, HIGH);
  digitalWrite(rightFrontRed, LOW);
  digitalWrite(leftFrontGreen, LOW);
  digitalWrite(leftFrontRed, LOW);
  digitalWrite(leftRearGreen, LOW);
  digitalWrite(leftRearRed, LOW);
  digitalWrite(rightRearGreen, HIGH);
  digitalWrite(rightRearRed, LOW);
}

void rightTurn(){
  digitalWrite(rightFrontGreen, LOW);
  digitalWrite(rightFrontRed, LOW);
  digitalWrite(leftFrontGreen, HIGH);
  digitalWrite(leftFrontRed, LOW);
  digitalWrite(leftRearGreen, HIGH);
  digitalWrite(leftRearRed, LOW);
  digitalWrite(rightRearGreen, LOW);
  digitalWrite(rightRearRed, LOW);
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
