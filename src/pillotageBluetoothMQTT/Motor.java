package pillotageBluetoothMQTT;

import lejos.robotics.RegulatedMotor;

public class Motor {
	private RegulatedMotor motor;
	private int speed;

	public Motor(RegulatedMotor _motor) {
		this.motor =_motor;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
		motor.setSpeed(speed);
	}

	public void movingBackward() {
//		setSpeed(50);
		motor.backward();
	}
	
	public void movingForward() {
//		setSpeed(50);
		motor.forward();
	}
	
	public RegulatedMotor getMotor() {
		return motor;
	}

	public void stop() {
		motor.stop();
	}

	public void rotateHalf() {
		motor.rotate(90);
	}
	
	public void speedUp(int value) {
		motor.setSpeed(speed + 50);
	}

	public void speedDown(int value) {
		motor.setSpeed(speed - 50);
	}

}
