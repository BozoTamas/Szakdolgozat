distance = measureDistance();
if(distance < 10){
  stopAction();
  delay(100);
  ObstacleAvoidance();
}

void ObstacleAvoidance(){ 
  //Az akadály érzékelése után egy kicsit hátrál a robot
  backwardAction(100);
  delay(250);
  stopAction();  
  //Megvizsgáljuk, a jobb oldalt
  myservo.write(0);
  delay(100);
  rightObstacle = measureDistance();
  delay(100);
  //Megvizsgáljuk a bal oldalt is
  myservo.write(180);
  delay(100);
  leftObstacle = measureDistance();
  delay(100);
  
  if(leftObstacle < rightObstacle){ avoidObstacleToRight(); }
  else{ avoidObstacleToLeft(); } 
}
