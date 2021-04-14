package com.example.bluetoothcontrol;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class AutoFollowActivity extends Activity {
    private UUID mDeviceUUID;
    private BluetoothSocket mBTSocket;
    private ReadInput mReadThread = null;

    private boolean mIsUserInitiatedDisconnect = false;
    private boolean mIsBluetoothConnected = false;

    private BluetoothDevice mDevice;

    final static String autoFollowCode = "222";
    final static char startFollow = 'S';
    final static char endFollow = 'E';
    final static char exitFollow = 'B';

    Intent help;

    private ProgressDialog progressDialog;
    ImageButton helpButton, homeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_follow);

        ActivityHelper.initialize(this);
        helpButton = (ImageButton)findViewById(R.id.helpButton);
        homeButton = (ImageButton)findViewById(R.id.homeButton);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        mDevice = b.getParcelable(MainActivity.DEVICE_EXTRA);
        mDeviceUUID = UUID.fromString(b.getString(MainActivity.DEVICE_UUID));

        manageComponents();

        help = new Intent(AutoFollowActivity.this, HelpActivity.class);

        helpButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                try {
                    mBTSocket.getOutputStream().write(exitFollow);
                    startActivity(new Intent(AutoFollowActivity.this, HelpActivity.class));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mBTSocket.getOutputStream().write(exitFollow);
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void manageComponents() {
        Button startButton = (Button) findViewById(R.id.startButton);
        Button stopButton = (Button) findViewById(R.id.stopButton);

        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    mBTSocket.getOutputStream().write(startFollow);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mBTSocket.getOutputStream().write(endFollow);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

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
                        int i = 0;

                        for (i = 0; i < buffer.length && buffer[i] != 0; i++) {
                        }
                        final String strInput = new String(buffer, 0, i);
                    }
                    Thread.sleep(500);
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
            if (mIsUserInitiatedDisconnect) {
                finish();
            }
        }

    }

    private void msg(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /*Amikor a képernyőről elnavigálunk, de nem kerül bezárásra a képernyő (pl. a telefon 'home' gombját nyomjuk meg, vagy lezárjuk a telefont)
      akkor a bluetooth kapcsolat megszakításra kerül, viszont amint vissszatérünk erre a képernyőre a csatléakozás automatikus újból végbemegy.*/

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

    //A bluetooth csatlakozási folyamat
    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean mConnectSuccessful = true;

        @Override
        protected void onPreExecute() {
            //A csatlakozási folyamat közben állapotjelző futtatása
            progressDialog = ProgressDialog.show(AutoFollowActivity.this, "Kérlek várj", "Bluetooth csatlakozás folyamatban");
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
                msg("A csatlakozás sikertelen. Ellenőrizd az eszközt melyhez csatlakozni szeretnél!");
                finish();
            } else {
                msg("Csatlakozva eszközhöz!");
                mIsBluetoothConnected = true;
                mReadThread = new ReadInput();
                try {
                    mBTSocket.getOutputStream().write(autoFollowCode.getBytes()); //Amint megtörténik a csatlakozás elküldünk a robotnak egy adatot, amivel jelezzük, hogy milyen módban kell futnia
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