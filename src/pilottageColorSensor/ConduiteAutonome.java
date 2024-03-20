package pilottageColorSensor;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import pilottageMQTT.Action;
import pilottageMQTT.MotorSync;

public class ConduiteAutonome {
	private NXTRegulatedMotor leftMotor, rightMotor;
	private EV3ColorSensor colorSensor;
	private EV3 ev3;
	private TextLCD lcd;
  private static  EV3UltrasonicSensor  ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S4);
  private Automate automate;
  private Boolean isdoing = true;
	public ConduiteAutonome(Automate automate) {

		ev3 = (EV3) BrickFinder.getLocal();
		lcd = ev3.getTextLCD();      
		 
		 this.automate = automate;
	}
	

	
	public void execute() {
	 // System.out.print(automate.getAutomate().get("1").getAction());
	    execute_Action("1");
	}
	
	public void  execute_Action(String next_etat) {
		if(automate.getAutomate().get(next_etat).getAction()==Action.STOP) return ;
		
		  MotorSync.startMotorsSync(Motor.B, Motor.C, automate.getAutomate().get(next_etat).getAction(), automate.getAutomate().get(next_etat).getTemps());
		  Delay.msDelay(500);
		  execute_Action(automate.getAutomate().get(next_etat).getEtat_Destination());
	}
	
	public boolean detectObstacle() {
	  
	   
	    SampleProvider distanceProvider = ultrasonicSensor.getMode("Distance");
	   
	    float[] distanceSample = new float[distanceProvider.sampleSize()];
	   
	    float distanceSeuil = 0.15f;

	    // Obtenir la distance mesurée
	    distanceProvider.fetchSample(distanceSample, 0);
	   
	    // La distance est stockée dans le premier élément du tableau
	  
	    float distance = distanceSample[0];
	   
	    // Afficher la distance mesurée
	  //  System.out.println("Distance: " + distance + " m");
	   
	  if (distance > distanceSeuil) {
	        return true;
	    } else {
	       
	        return false;
	    }
	}

   public void contourObstacle() {
	    leftMotor.stop();
		rightMotor.stop();
   }
}
