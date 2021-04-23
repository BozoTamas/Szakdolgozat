void forwardAction(int sebesseg) {
  leftFront.run(FORWARD);
  leftFront.setSpeed(sebesseg);
  rightFront.run(FORWARD);
  rightFront.setSpeed(sebesseg);
  rightRear.run(FORWARD);
  rightRear.setSpeed(sebesseg);
  leftRear.run(FORWARD);
  leftRear.setSpeed(sebesseg);
}
