#include <SoftwareSerial.h>
SoftwareSerial myBluetooth(2,13); //Példányosítjuk a HC-05-ös modult az Rx és Tx-ként használt tüskék megadásával

void setup() {
  myBluetooth.begin(9600); //Beállítjuk a modul sávszélességét
}

void loop() {
  while(myBluetooth.available() == 0);
}
