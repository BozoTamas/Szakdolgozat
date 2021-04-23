if(digitalRead(leftIRSensor)==0 && digitalRead(rightIRSensor)==0){ forwardAction(100); }
else if(digitalRead(leftIRSensor)==0 && !digitalRead(rightIRSensor)==0){ rightAction(100,130); }
else if(!digitalRead(leftIRSensor)==0 && digitalRead(rightIRSensor)==0){ leftAction(100,130); }
else if(!digitalRead(leftIRSensor)==0 && !digitalRead(rightIRSensor)==0){ stopAction(); }
