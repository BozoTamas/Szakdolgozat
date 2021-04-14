if(digitalRead(leftIRSensor)==0 && digitalRead(rightIRSensor)==0){ forwardAction(); }
else if(digitalRead(leftIRSensor)==0 && !digitalRead(rightIRSensor)==0){ rightAction(); }
else if(!digitalRead(leftIRSensor)==0 && digitalRead(rightIRSensor)==0){ leftAction(); }
else if(!digitalRead(leftIRSensor)==0 && !digitalRead(rightIRSensor)==0){ stopAction(); }
