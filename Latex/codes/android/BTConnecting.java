private class ConnectBT extends AsyncTask<Void, Void, Void> {
  private boolean mConnectSuccessful = true;
	
  @Override
  protected Void doInBackground(Void... devices) {
    try {
      if (mBTSocket == null || !mIsBluetoothConnected) {
        ...Kapcsolódunk eszközhöz
      }
    } catch (IOException e) {
        mConnectSuccessful = false;
	  }
	  return null;
  }
}