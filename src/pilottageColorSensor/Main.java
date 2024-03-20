package pilottageColorSensor;
import java.io.File;

import lejos.hardware.Sound;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Main {

	public static void main(String[] args) {
		
	    ConduiteByColor m = new  ConduiteByColor();
	      
	  
		try {
		     m.test_avec_couleur();
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		} catch (RuntimeException e) {
			System.out.println("Program stopped.");
		} finally {
			Delay.msDelay(1500);
		}
		
		System.exit(0);
		
	}
}
