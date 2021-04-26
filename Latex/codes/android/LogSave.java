<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /> //Az AndroidManifest-hez hozzáadandó kód

if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) //Megvizsgáljuk, hogy az alkalmazás rendelkezik-e a szükséges engedéllyel
    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1000);

SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss", Locale.getDefault());
File_name = "Naplófájl: " + sdf.format(new Date()) + ".txt"; //A naplófájl nevének megadása

FileOutputStream myStream = null;

myStream = openFileOutput(File_name, MODE_PRIVATE);//Létrehozunk a telefonon egy új fájlt az eltárolt névvel
OutputStreamWriter myWriter = new OutputStreamWriter(myStream);
myWriter.write(logViewContent); //Fájl tartalmának módosítása
myWriter.flush();
myWriter.close();
msg("Napló mentve: " + File_name + " néven!", 1);