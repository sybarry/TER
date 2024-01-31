import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;

public class ConduiteAutonome {
	private 	NXTRegulatedMotor leftMotor,  rightMotor ;
	private EV3ColorSensor colorSensor;
	private EV3 ev3;
	private  	TextLCD lcd;
   public ConduiteAutonome() {
		 leftMotor = Motor.B;
		  rightMotor = Motor.C;
		 colorSensor = new EV3ColorSensor(SensorPort.S3);
		 ev3 = (EV3) BrickFinder.getLocal();
	     lcd = ev3.getTextLCD();
		
		// Initialize sampleFetcher
   }
   public void test() {
	   float redSample[];
		SensorMode redMode = colorSensor.getRedMode();
		redSample = new float[redMode.sampleSize()];
		
		// Hard-coded values
		
		float lower = 0.02f;
		float upper = 0.10f;
		
		// Start moving the robot
		//leftMotor.backward(); // backward because of gears
		//rightMotor.backward();
		leftMotor.setSpeed(400);
		rightMotor.setSpeed(400);
		
		while (true) {
			redMode.fetchSample(redSample, 0);
			
			// Output sample data
			lcd.clear();
			lcd.drawString(String.valueOf(redSample[0]), 1, 3);
			
			// Correct direction
			if (lower <= redSample[0] && redSample[0] <= upper) {
			
				leftMotor.forward();
				rightMotor.forward();
				
			}
			else if (redSample[0] < lower) { 
				leftMotor.setSpeed(100);
				rightMotor.setSpeed(100);
				leftMotor.backward();
				rightMotor.stop();
			}
			else if (redSample[0] > upper) { 
				leftMotor.setSpeed(100);
				rightMotor.setSpeed(100);
				leftMotor.stop();
				rightMotor.backward();
			}
			
			// Allow for some time before self-correcting
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {}
		}
  }
   
}
