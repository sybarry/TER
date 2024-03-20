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

public class ConduiteByColor {
	
	private NXTRegulatedMotor leftMotor, rightMotor;
	private EV3ColorSensor colorSensor;
	private EV3 ev3;
	private TextLCD lcd;
  private  EV3UltrasonicSensor  ultrasonicSensor;
  private Automate automate;
  private Boolean isdoing = true;
  
  private final float distanceSeuil = 0.24f;
  SampleProvider distanceProvider;
  float[] distanceSample ;
  
    public ConduiteByColor() {
    	     leftMotor = Motor.B; // Moteur Gauche
    		 rightMotor = Motor.C; //Moteur Droit
    			colorSensor = new EV3ColorSensor(SensorPort.S3);
    			ev3 = (EV3) BrickFinder.getLocal();
    			lcd = ev3.getTextLCD();
    			 ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S4);
    }
 
    	public void test_avec_couleur() throws InterruptedException, RuntimeException {
    		float redSample[];
    		SensorMode redMode = colorSensor.getRedMode();
    		redSample = new float[redMode.sampleSize()];

    		// Permet de jaugé la couleur pour connaitre une intervalle
    		float lower = 0.02f;
    		float upper = 0.10f;
    		

   
    		

    		while (true) {
    			redMode.fetchSample(redSample, 0);

    			// Affiche les couleurs
    			lcd.clear();
    			lcd.drawString(String.valueOf(redSample[0]), 1, 3);
    			vitesse(200);
    			// La bonne direction
    			if(detectObstacle()) {
    				if (lower <= redSample[0] && redSample[0] <= upper) {
    					
    					leftMotor.forward();
    					rightMotor.forward();
    					
    				}
    				else if (redSample[0] < lower) { 
    					vitesse(100);
    					leftMotor.backward();
    					rightMotor.stop();
    				}
    				else if (redSample[0] > upper) { 
    					vitesse(100);
    					leftMotor.stop();
    					rightMotor.backward();
    				}
    				
    				// Allow for some time before self-correcting
    				try {
    					Thread.sleep(50);
    				} catch (InterruptedException e) {}

    			Thread.sleep(50);
    			if (Button.DOWN.isDown())
    				throw new RuntimeException("Program stopped by user");
    		}else {
    			contourObstacle();
    		}
    		}
    	
    	
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
    		vitesse(100);
			
    	    rightMotor.stop();
    	    leftMotor.forward();
    	    Delay.msDelay(1500);
    	     
    	     leftMotor.forward();
    	     rightMotor.forward();
    	     Delay.msDelay(2500);
    	     
    	    leftMotor.stop();
    	    rightMotor.forward();
    	    
    	    Delay.msDelay(1500);
       }
       public void vitesse(int v) {
    		leftMotor.setSpeed(v);
			rightMotor.setSpeed(v);
       }
   
}
