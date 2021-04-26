package com.example.bluetoothcontrol;

//Konténerosztály, ami a súgóhoz tartozó szövegeket tartalmazza. A súgóban a hiváskor visszaadjuk az előre beállított szöveget

public class Instructions {
    private String followInstruction = "A főmenüben a 'követés' gombot megnyomva a robot önműködő módba lép, ahol az előre megadott vonalat követi.\nAmennyiben a vonal követése közben a robot útjába akadály kerül a robot ezt kikerüli.\nA kikerülés előtt a robot megvizsgálja, hogy jobbra vagy balra tudja megtenni az akadály kikerülését, majd az akadály kikerülése után újból megkeresi a követendő vonalat.";
    private String controlInstruction = "A főmenüben az 'irányítás' gombot megnyomva a robotot az okostelefon segítségével irányíthatjuk.\nElőre: A robot előree halad, míg a gombot nyomva tartjuk.\nBalra: A robot az óramutató járásával ellentétes irányba fordul.\nJobbra: A robot az óramutató járásával megegyező irányba fordul.\nHátra: A robot tolat, amíg a gombot nyomva tartjuk.\nTest car: A robot egy tesztet végez el, melynek során a motorok lehetséges mozgását szimulálja.";
    private String logInstruction = "A robot működés közben a bekövetkezett eseményeket egy változóban tárolja, amit a felhasználó lekérhet, valamint utasíthatja a robotot a változó kiürítésére.\nA log fájl lekéréséhez a kezdőoldalon a HC-05 modul kiválasztása után a 'log' gombot megnyomva válik elérhetővé a log felület, ahol a naplófájl lekérhető/törölhető és a telefonra menthető.\nA fájl mentése <Naplófájl: Év_Hó_Nap> formában történik.";

    public String getFollowInstruction(){
        return followInstruction;
    }

    public String getControlInstruction(){
        return controlInstruction;
    }

    public String getLogInstruction(){return logInstruction;}
}
