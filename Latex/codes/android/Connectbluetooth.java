devices.setOnClickListener(new View.OnClickListener() {

    @Override
    public void onClick(View arg0) {
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBTAdapter == null) {
            Toast.makeText(getApplicationContext(), "A bluetooth nincs bekapcsolva", Toast.LENGTH_SHORT).show();
        } else if (!mBTAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); //Meghívjuk a bekapcsolási kérelmet
            startActivityForResult(enableBT, BT_ENABLE_REQUEST);
        } else {
            new SearchDevices().execute();
        }
    }
});

follow.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View arg0) {
        BluetoothDevice device = ((MyAdapter) (listView.getAdapter())).getSelectedItem(); //Eltároljuk a listából kiválasztott eszközt
        Intent intent = new Intent(getApplicationContext(), AutoFollowActivity.class);
        intent.putExtra(DEVICE_EXTRA, device); 				  
        intent.putExtra(DEVICE_UUID, mDeviceUUID.toString()); 
        intent.putExtra(BUFFER_SIZE, mBufferSize); //Az új activity indításakor a választott eszköz paramétereit is átatdjuk
        startActivity(intent);
    }
});