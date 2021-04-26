-- Arduino kód --
void logEvent(String event){ logFile += event; }
myBluetooth.println(logFile); //Az adat küldése a telefonra

-- Android kód --
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
        logTextView.setText(displayedLog);//A fogadott adat formázása és megjelenítése
    }
    t.sleep(500);
}//A bejövő adat fogadása