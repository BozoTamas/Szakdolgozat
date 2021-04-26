private UUID mDeviceUUID;
private BluetoothDevice mDevice;
private BluetoothSocket mBTSocket;

mDevice = b.getParcelable(MainActivity.DEVICE_EXTRA);
mDeviceUUID = UUID.fromString(b.getString(MainActivity.DEVICE_UUID));

mBTSocket = mDevice.createInsecureRfcommSocketToServiceRecord(mDeviceUUID);
mBTSocket.connect();