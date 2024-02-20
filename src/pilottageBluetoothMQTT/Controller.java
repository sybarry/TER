package pilottageBluetoothMQTT;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;

public class Controller {
	public static State actualState = State.STOPPED;
	private final Motor leftMotor;
    private final Motor rightMotor;
    
	public Controller()
	{
		this.leftMotor = new Motor(new EV3LargeRegulatedMotor(MotorPort.B));
	    this.rightMotor = new Motor(new EV3LargeRegulatedMotor(MotorPort.C));
    	RegulatedMotor[] T = {this.rightMotor.getMotor()};
    	leftMotor.getMotor().synchronizeWith(T);
	}

	public void movingForward () {
		if(actualState == State.STOPPED) {			
	    	leftMotor.getMotor().startSynchronization();
	    	leftMotor.movingForward();
	        rightMotor.movingForward();
	        actualState = State.FORWARD;
	        leftMotor.getMotor().endSynchronization();
		} else if(actualState == State.BACKWARD) {
			System.out.println("Must stop first");
		} 
	}
	
	public void movingBackward() {
		if(actualState == State.STOPPED) {			
	    	leftMotor.getMotor().startSynchronization();
	    	leftMotor.movingBackward();
	    	rightMotor.movingBackward();
	        actualState = State.BACKWARD;
	        leftMotor.getMotor().endSynchronization();
		} else if(actualState == State.FORWARD) {
			System.out.println("Must stop first");
		}
	}
	
	public void stop() {
		if(actualState == State.FORWARD) {
	    	leftMotor.getMotor().startSynchronization();
	    	leftMotor.stop();
	    	rightMotor.stop();
	        actualState = State.STOPPED;
	        leftMotor.getMotor().endSynchronization();
		} else if(actualState == State.BACKWARD) {
	    	leftMotor.getMotor().startSynchronization();
	    	leftMotor.stop();
	    	rightMotor.stop();
	        actualState = State.STOPPED;
	        leftMotor.getMotor().endSynchronization();
		} else if(actualState == State.SLOWING_DOWN) {
	    	leftMotor.getMotor().startSynchronization();
	    	leftMotor.stop();
	    	rightMotor.stop();
	        actualState = State.STOPPED;
	        leftMotor.getMotor().endSynchronization();
		} else if(actualState == State.BACKUP) {
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

	public void slowDown() {
		if(actualState == State.FORWARD) {
			leftMotor.setPreviousSpeed(leftMotor.getSpeed());
			rightMotor.setPreviousSpeed(rightMotor.getSpeed());
			actualState = State.SLOWING_DOWN ;
			leftMotor.getMotor().startSynchronization();
	    	leftMotor.setSpeed(30);
	        rightMotor.setSpeed(30);
	        leftMotor.getMotor().endSynchronization();
		}
	}

}