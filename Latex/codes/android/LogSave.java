SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss", Locale.getDefault());
File_name = "Naplófájl: " + sdf.format(new Date()) + ".txt"; //A naplófájl nevének megadása

FileOutputStream myStream = null;

myStream = openFileOutput(File_name, MODE_PRIVATE); //Fájl létrehozása
OutputStreamWriter myWriter = new OutputStreamWriter(myStream);
myWriter.write(logViewContent); //Fájl tartalmának módosítása
myWriter.flush();
myWriter.close();
msg("Napló mentve: " + File_name + " néven!", 1);