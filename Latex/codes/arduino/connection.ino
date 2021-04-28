#include <SoftwareSerial.h>
SoftwareSerial myBluetooth(2,13); //Új soros portot példányosítunk a HC-05-ös modul számára az Rx és Tx-ként használt tüskék megadásával

void setup() {
  myBluetooth.begin(9600); //Beállítjuk a modul sávszélességét
}

void loop() {
  while(myBluetooth.available() == 0);
}
