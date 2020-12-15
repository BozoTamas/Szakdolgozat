#define echoPin 2
#define trigPin 3
#define led_1 4
#define led_2 5
#define led_3 6
#define led_4 7

long duration;
int distance;

void setup() {
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
  pinMode(led_1, OUTPUT);
  pinMode(led_2, OUTPUT);
  pinMode(led_3, OUTPUT);
  pinMode(led_4, OUTPUT);
  Serial.begin(9600);
}

void loop() {

  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);

  duration = pulseIn(echoPin, HIGH);

  distance = duration * 0.034 / 2;
  if(distance > 15){
    digitalWrite(led_1,HIGH);
    digitalWrite(led_2,HIGH);
    digitalWrite(led_3,LOW);
    digitalWrite(led_4,LOW);    
  }
  else{
    pulsateRED();
  }
  Serial.print("Distance: ");
  Serial.print(distance);
  Serial.println(" cm");
}

void pulsateRED() {
    digitalWrite(led_1,LOW);
    digitalWrite(led_2,LOW);
    digitalWrite(led_3,HIGH);
    digitalWrite(led_4,HIGH);
    delay(300);
    digitalWrite(led_3,LOW);
    digitalWrite(led_4,LOW);
    delay(300); 
}
