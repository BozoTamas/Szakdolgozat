-- Arduino kód --
void logEvent(String event){ logFile += event; }
myBluetooth.println(logFile); //Az adat küldése a telefonra

-- Android kód --
inputStream = mBTSocket.getInputStream();
while (!bStop) {
    byte[] buffer = new byte[256];
    if (inputStream.available() > 0) {
        inputStream.read(buffer);
        int i = 0;
        for (i = 0; i < buffer.length && buffer[i] != 0; i++) {}
        final String strInput = new String(buffer, 0, i);
        displayedLog = strInput;
    }
    Thread.sleep(500);
} //A bejövő adat fogadása

displayedLog = displayedLog.replaceAll("_", "\n" + "> "); //A napló formázása és megejelenítése
logTextView.setText(displayedLog);