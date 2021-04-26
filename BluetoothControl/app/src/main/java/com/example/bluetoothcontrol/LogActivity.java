package com.example.bluetoothcontrol;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class LogActivity extends Activity {
    private UUID mDeviceUUID;
    private BluetoothSocket mBTSocket;
    private ReadInput mReadThread = null;

    private boolean mIsBluetoothConnected = false;

    private static String File_name;
    private String displayedLog = "";

    private BluetoothDevice mDevice;

    final static String logCode = "333";
    final static char getLog = 'G';
    final static char deleteLog = 'D';
    final static char exitLog = 'B';

    Intent help;

    private ProgressDialog progressDialog;
    ImageButton helpButton, homeButton;
    TextView logTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        ActivityHelper.initialize(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1000);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss", Locale.getDefault());
        File_name = "Naplófájl: " + sdf.format(new Date()) + ".txt";

        helpButton = findViewById(R.id.helpButton);
        homeButton = findViewById(R.id.homeButton);
        logTextView = findViewById(R.id.logTextView);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();


        manageComponents();

        help = new Intent(LogActivity.this, HelpActivity.class);

        helpButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                showHelpButtonAlert();
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHomeButtonAlert();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1000:
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    //Ha az engedélyt nem adja meg a felhasználó egy Toast üzenet jelzi ezt
                    msg("Hozzáférés megtagadva!",0);
                }
                else{
                    //Az engedély megadását is jelzi üzenet
                    msg("Hozzáférés megadva!",0);
                }
        }
    }

    private void manageComponents(){
        Button logDeleteButton = findViewById(R.id.logDeleteButton);
        Button logSaveButton = findViewById(R.id.logSaveButton);
        Button logGetButton = findViewById(R.id.logGet);


        logGetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mBTSocket.getOutputStream().write(getLog);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        //Kiürítjük a Log-ot
        logDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mBTSocket.getOutputStream().write(deleteLog);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                logTextView.setText("");
                msg("Napló ürítve!",0);
            }
        });

        //A log tartalmát mentjük
        logSaveButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && !(logTextView.getText().toString().length() < 3))  { //Megvizsgáljuk, hogy a felhasználó megadta-e az engedélyt a mentésre és a Log tartalmaz-e valamit
                    String logViewContent = logTextView.getText().toString(); //Ha igen, a Log tartalmát kimentjük egy változóba

                    FileOutputStream myStream = null;

                    try { //Lementjük a változóban eltárolt adatot
                        myStream = openFileOutput(File_name, MODE_PRIVATE);
                        OutputStreamWriter myWriter = new OutputStreamWriter(myStream);

                        myWriter.write(logViewContent);
                        myWriter.flush();
                        myWriter.close();

                        msg("Napló mentve: " + File_name + " néven!", 1);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        msg("Hiba a fájl létrehozásakor!",0);
                    } catch (IOException e) {
                        e.printStackTrace();
                        msg("Hiba a mentés elvégzésekor!",0);
                    } finally {
                        if (myStream != null) {
                            try {
                                myStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                else if (logTextView.getText().toString().length() < 3){ //Ha a log tartalma üres egy Pop-Up üzenettel jelezzük a felhasználónak
                    showLogEmptyAlert();
                }
                else{ //Ha nincs engedély adva a felhasználónak jelezzük
                    showPermissionDeniedAlert();
                }
            }
        });
    }
    private void showHomeButtonAlert(){
        AlertDialog.Builder saveAlert = new AlertDialog.Builder(LogActivity.this);

        saveAlert.setCancelable(false);
        saveAlert.setTitle("Biztosan ki akar lépni?");
        saveAlert.setMessage("Előfordulhat, hogy a Log tartalma még nem került mentésre!");

        saveAlert.setNegativeButton("Nem", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        saveAlert.setPositiveButton("Igen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    mBTSocket.getOutputStream().write(exitLog);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
        saveAlert.show();
    }

    //Amikor a felhasználó megnyomja a 'Súgó' gombot, visszajelzést kérünk
    private void showHelpButtonAlert(){
        AlertDialog.Builder helpAlert = new AlertDialog.Builder(LogActivity.this);

        helpAlert.setCancelable(false);
        helpAlert.setTitle("Biztosan ki akar lépni?");
        helpAlert.setMessage("Előfordulhat, hogy a Log tartalma még nem került mentésre!");

        helpAlert.setNegativeButton("Nem", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        helpAlert.setPositiveButton("Igen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    mBTSocket.getOutputStream().write(exitLog);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(LogActivity.this, HelpActivity.class));
                finish();
            }
        });
        helpAlert.show();
    }

    //Amennyiben az engedély nincs megadva ez az üzenet jelenik meg
    private void showPermissionDeniedAlert(){
        AlertDialog.Builder permissionAlert = new AlertDialog.Builder(LogActivity.this);

        permissionAlert.setCancelable(false);
        permissionAlert.setTitle("Hozzáférés megtagadva!");
        permissionAlert.setMessage("Nincs felhatalmazva a mentésre!\nAz engedély megadását a beállításokban tudja megtenni.");

        permissionAlert.setPositiveButton("Tovább", new DialogInterface.OnClickListener() { //A 'Tovább' lehetőségre nyomva megjelenik a következő üzenet
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionGrantDialog();
            }
        });
        permissionAlert.show();
    }

    private void permissionGrantDialog(){
        AlertDialog.Builder settingsAlert = new AlertDialog.Builder(LogActivity.this);

        settingsAlert.setCancelable(false);
        settingsAlert.setTitle("Hozzáférés megtagadva!");
        settingsAlert.setMessage("Beállítások -> Alkalmazások -> RobotControlApp -> Engedélyek");

        settingsAlert.setPositiveButton("Megadtam az engedélyt!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    //Ellenőrizzük, hogy a felhasználó ténylegesen megadta-e az engedélyt
                    dialog.cancel();
                }
                else showPermissionDeniedAlert();
            }
        });

        settingsAlert.setNegativeButton("Segítségre van szükségem", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showAdminDialog();
            }
        });
        settingsAlert.show();
    }

    private void showAdminDialog(){
        AlertDialog.Builder adminAlert = new AlertDialog.Builder(LogActivity.this);

        adminAlert.setCancelable(false);
        adminAlert.setTitle("Hozzáférés megtagadva!");
        adminAlert.setMessage("Segítségért forduljon xy-hoz.");

        adminAlert.setNegativeButton("Kilépés a főmenübe", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        adminAlert.show();
    }

    //Ha a Log nem tartalmaz semmit a felhasználó figyelmeztetésre kerül
    private void showLogEmptyAlert(){
        AlertDialog.Builder logEmptyAlert = new AlertDialog.Builder(LogActivity.this);

        logEmptyAlert.setCancelable(false);
        logEmptyAlert.setTitle("Sikertelen mentés!");
        logEmptyAlert.setMessage("A log tartalma hibás!");

        logEmptyAlert.setPositiveButton("Rendben", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        logEmptyAlert.show();
    }

    private class ReadInput implements Runnable {

        private boolean bStop = false;
        private Thread t;

        public ReadInput() {
            t = new Thread(this, "Input Thread");
            t.start();
        }

        public boolean isRunning() {
            return t.isAlive();
        }

        @Override
        public void run() {
            InputStream inputStream;

            try {
                inputStream = mBTSocket.getInputStream();
                while (!bStop) {
                    byte[] buffer = new byte[256];
                    if (inputStream.available() > 0) {
                        inputStream.read(buffer);
                        int i;

                        for (i = 0; i < buffer.length && buffer[i] != 0; i++) {
                            displayedLog = new String(buffer, 0, i);
                        }
                        displayedLog = displayedLog.replaceAll("_", "\n" + "> ");
                        logTextView.setText(displayedLog);
                    }
                    t.sleep(500);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void stop() {
            bStop = true;
        }

    }

    private class DisConnectBT extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            if (mReadThread != null) {
                mReadThread.stop();
                while (mReadThread.isRunning()) ;
                mReadThread = null;
            }

            try {
                mBTSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        //Amikor a csatlakozási folyamatnak vége, megvizsgálásra kerül, hogy a csatlakozás végbement-e
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mIsBluetoothConnected = false;
            boolean mIsUserInitiatedDisconnect = false;
            if (mIsUserInitiatedDisconnect) {
                finish();
            }
        }

    }

    private void msg(String message, int duration) {
        if(duration == 0){
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
        else if(duration == 1){
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    /*Amikor a képernyőről elnavigálunk, de nem kerül bezárásra a képernyő (pl. a telefon 'home' gombját nyomjuk meg, vagy lezárjuk a telefont)
      akkor a bluetooth kapcsolat szüneteltetésre kerül, viszont amint vissszatérünk erre a képernyőre a csatlakozás automatikusan végbemegy az ereedetileg eltárolt adatokkal.*/

    @Override
    protected void onPause() {
        if (mBTSocket != null && mIsBluetoothConnected) {
            new DisConnectBT().execute();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mBTSocket == null || !mIsBluetoothConnected) {
            new ConnectBT().execute();
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    //A bluetooth csatlakozási folyamat ebben az osztályban megy végbe
    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean mConnectSuccessful = true;

        @Override
        protected void onPreExecute() {
            //A csatlakozási folyamat közben jelző futtatása
            progressDialog = ProgressDialog.show(LogActivity.this, "Kérlek várj", "Bluetooth csatlakozás folyamatban");
        }

        @Override
        protected Void doInBackground(Void... devices) {
            //A háttérben megy végbe a kapcsolódás. Az elsődleges feladat amit látunk a 'progressDialog'
            try {
                if (mBTSocket == null || !mIsBluetoothConnected) { //Ha a bluetooth csatlakozás sikeresen végbe ment
                    mBTSocket = mDevice.createInsecureRfcommSocketToServiceRecord(mDeviceUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    mBTSocket.connect();
                }
            } catch (IOException e) { //Ha a bluetooth csatlakozás sikertelen volt
                mConnectSuccessful = false;
            }
            return null;
        }

        //A csatlakozás utáni visszajelzés
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (!mConnectSuccessful) {
                msg("A csatlakozás sikertelen. Ellenőrizd az eszközt melyhez csatlakozni szeretnél!",0);
                finish();
            } else {
                msg("Csatlakozva eszközhöz!",0);
                mIsBluetoothConnected = true;
                mReadThread = new ReadInput();
                try {
                    mBTSocket.getOutputStream().write(logCode.getBytes()); //Ha csatlakoztunk jelezzük a robotnak, hogy logolni akarunk
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            progressDialog.dismiss();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}