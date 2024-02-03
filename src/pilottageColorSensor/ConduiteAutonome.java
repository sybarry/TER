package pilottageColorSensor;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import pilottageMQTT.Action;
import pilottageMQTT.MotorSync;

public class ConduiteAutonome {
	private NXTRegulatedMotor leftMotor, rightMotor;
	private EV3ColorSensor colorSensor;
	private EV3 ev3;
	private TextLCD lcd;

	public ConduiteAutonome() {
		leftMotor = Motor.B;
		rightMotor = Motor.C;
		colorSensor = new EV3ColorSensor(SensorPort.S3);
		ev3 = (EV3) BrickFinder.getLocal();
		lcd = ev3.getTextLCD();

		// Initialize sampleFetcher
	}

	public void test() throws InterruptedException, RuntimeException {
		float redSample[];
		SensorMode redMode = colorSensor.getRedMode();
		redSample = new float[redMode.sampleSize()];

		// Permet de jaugé la couleur pour connaitre une intervalle
		float lower = 0.02f;
		float upper = 0.10f;

		// leftMotor.backward(); // backward because of gears
		// rightMotor.backward();
		leftMotor.setSpeed(100);
		rightMotor.setSpeed(100);

		while (true) {
			redMode.fetchSample(redSample, 0);

			// Affiche les couleurs
			lcd.clear();
			lcd.drawString(String.valueOf(redSample[0]), 1, 3);

			// La bonne direction
			if (lower <= redSample[0] && redSample[0] <= upper) {
				MotorSync.startMotorsSync(rightMotor, leftMotor, Action.FORWARD, 0);
			} else if (redSample[0] < lower) {
				// sync this one
				leftMotor.backward();
				rightMotor.stop();
			} else if (redSample[0] > upper) {
				// sync this one
				leftMotor.stop();
				rightMotor.backward();
			}

			Thread.sleep(50);
			if (Button.DOWN.isDown())
				throw new RuntimeException("Program stopped by user");
		}
	}

}
