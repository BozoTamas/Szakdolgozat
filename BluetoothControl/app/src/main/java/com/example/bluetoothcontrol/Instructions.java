package com.example.bluetoothcontrol;

//Konténerosztály, ami a súgóhoz tartozó szövegeket tartalmazza. A súgóban a hiváskor visszaadjuk az előre beállított szöveget

public class Instructions {
    private String followInstruction = "A főmenüben az 'Automata követés' gombot megnyomva a robot önműködő módba lép, ahol az előre megadott vonalat követi.\nEkkor a robot a megtörtént eseményeket" +
            "egy 'napló' fájlban elmenti és ezt a naplófájlt a felhasználó az applikáció segítségével lekérheti és a telefonon mentheti.\nA log fájl lekéréséhez a kezdőoldalon a HC-05 modul kiválasztása után a 'log' gombot megnyomva" +
            " válik elérhetővé a log felület, ahol a naplófájl lekérhető/törölhető és a telefonra menthető.\nA fájl mentése <Naplófájl: Év_Hó_Nap> formában történik.";
    private String controlInstruction = "A főmenüben a 'Bluetooth irányítás' gombot megnyomva a robotot az okostelefon segítségével irányíthatjuk.\nA következőkben a választható irányokról olvashatnak a sorokon fentről-lefele haladva, balról-jobbra." +
            "\nElső gomb: A motorok előre forognak, a jobb oldaliak nagyobb nyomatékkal.\nMásodik gomb: A motorok előre forognak egyező nyomatékkal.\nHarmadik gomb: A motorok előre forognak, a bal oldaliak nagyobb nyomatékkal." +
            "\nNyegyedik gomb: A bal motorok hátra, a jobb motorok előre forognak.\nÖtödik gomb: - - -\nHatodik gomb: A jobb motorok hátra, a bal motorok előre forognak." +
            "\nHetedik gomb: A motorok hátra forognak, a jobb oldaliak nagyobb nyomatékkal.\nNyolcadik gomb: A motorok hátra forognak egyező nyomatékkal.\nKilencedik gomb: A motorok hátra forognak, a bal oldaliak nagyobb nyomatékkal.";

    public String getFollowInstruction(){
        return followInstruction;
    }

    public String getControlInstruction(){
        return controlInstruction;
    }

}
