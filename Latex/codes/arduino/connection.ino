#include <SoftwareSerial.h>
SoftwareSerial myBluetooth(2,13);

void setup() {
  myBluetooth.begin(9600);
}

void loop() {
  while(myBluetooth.available() == 0);
}
