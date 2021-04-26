forward.setOnTouchListener(new View.OnTouchListener() {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                try {
                    mBTSocket.getOutputStream().write(forwardAction);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            case MotionEvent.ACTION_UP: //Az onClickListener() szintaxisa megegyezik ezzel
                try {
                    mBTSocket.getOutputStream().write(stopAction);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
			
        }//Nincs default ág, ugyanis egy gomb vagy le van nyomva, vagy nincs, ezért csak ezeket az eseteket kell kezelnünk
        return false;
    }
});