distance = measureDistance();
if(distance < 10){
  stopAction();
  delay(100);
  ObstacleAvoidance();
}

void ObstacleAvoidance(){ 
  //Az akadály érzékelése után egy kicsit hátrál a robot
  backwardAction();
  delay(250);
  stopAction();
  delay(100);
  
  //Megvizsgáljuk, a jobb oldalt
  myservo.write(0);
  delay(400);
  rightObstacle = measureDistance();
  delay(200);

  //Megvizsgáljuk a bal oldalt is
  myservo.write(180);
  delay(400);
  leftObstacle = measureDistance();
  delay(200);
  
  if(leftObstacle < rightObstacle){ //Ha a bal oldalon is van akadály
    avoidObstacleToRight();
  }
  else{ //Ha a jobb oldalon is van akadály
    avoidObstacleToLeft();
  } 
}
