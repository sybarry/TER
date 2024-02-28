package pillotageBluetoothMQTT;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;

public class Controller {
	State actualState = State.STOPPED;
	private Motor leftMotor;
    private Motor rightMotor;
    private int speedRight = 50;
    private int speedLeft = speedRight;

	public Controller()
	{
		this.leftMotor = new Motor(new EV3LargeRegulatedMotor(MotorPort.C));
	    this.rightMotor = new Motor(new EV3LargeRegulatedMotor(MotorPort.B));

    	RegulatedMotor[] T = {this.rightMotor.getMotor()};
    	leftMotor.getMotor().synchronizeWith(T);

    	leftMotor.setSpeed(speedLeft);
        rightMotor.setSpeed(speedRight);
	}

	public void movingForward () {
		if(actualState == State.STOPPED) {
	    	leftMotor.getMotor().startSynchronization();
	    	leftMotor.movingForward();
	        rightMotor.movingForward();
	        actualState = State.FORWARD;
	        leftMotor.getMotor().endSynchronization();
		} else System.out.println("Must stop first");
	}

	public void movingBackward() {
		if(actualState == State.STOPPED) {
	    	leftMotor.getMotor().startSynchronization();
	    	leftMotor.movingBackward();
	    	rightMotor.movingBackward();
	        actualState = State.BACKWARD;
	        leftMotor.getMotor().endSynchronization();
		} else System.out.println("Must stop first");
	}

	public void stop() {
		if(actualState != State.STOPPED) {
			leftMotor.getMotor().startSynchronization();
	    	leftMotor.stop();
	    	rightMotor.stop();
	        actualState = State.STOPPED;
	        leftMotor.getMotor().endSynchronization();
		}
	}

	public void turnLeft(int ratio) {
    	leftMotor.getMotor().startSynchronization();
    	leftMotor.setSpeed(leftMotor.getSpeed());
        rightMotor.setSpeed(rightMotor.getSpeed() * ratio);
        leftMotor.getMotor().endSynchronization();
	}

	public void turnRight(int ratio) {
		leftMotor.getMotor().startSynchronization();
    	leftMotor.setSpeed(leftMotor.getSpeed() * ratio);
        rightMotor.setSpeed(rightMotor.getSpeed());
        leftMotor.getMotor().endSynchronization();
	}

	public void accelerate(int value) {
		leftMotor.getMotor().startSynchronization();
    	leftMotor.setSpeed(leftMotor.getSpeed() + value);
        rightMotor.setSpeed(rightMotor.getSpeed() + value);
        leftMotor.getMotor().endSynchronization();
	}

	public void decelerated(int value) {
		if(leftMotor.getSpeed() - value <= 0 || rightMotor.getSpeed() - value <= 0) {
			leftMotor.getMotor().startSynchronization();
	    	leftMotor.stop();
	    	rightMotor.stop();
	        actualState = State.STOPPED;
	        leftMotor.getMotor().endSynchronization();
		} else {
			leftMotor.getMotor().startSynchronization();
	    	leftMotor.setSpeed(leftMotor.getSpeed() - value);
	        rightMotor.setSpeed(rightMotor.getSpeed() - value);
	        leftMotor.getMotor().endSynchronization();
		}
	}


}