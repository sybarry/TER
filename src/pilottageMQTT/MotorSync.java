package pilottageMQTT;

import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.utility.Delay;

public class MotorSync {

	// Move the motors at the same time with no delay (sync)
	public static void startMotorsSync(final NXTRegulatedMotor M1, final NXTRegulatedMotor M2, final Action action,
			final int duration) {
		
		
		  performMotorAction(M2,M1, action);
		  

	        
	
		
		

		
		
		if (duration != 0) {
			Delay.msDelay(duration); // Moving duration
			if (action != Action.STOP)
				startMotorsSync(M1, M2, Action.STOP, 300);
			// Stop the motors if the're moving
		}
		
	}

	// Custom action on given motor
	private static void performMotorAction(final NXTRegulatedMotor motor1, final NXTRegulatedMotor motor2, Action action) {
	    switch (action) {
	        case FORWARD:
	        	Thread M2_Go_Thread = new Thread(new Runnable() {
	                @Override
	                public void run() {
	                    motor2.forward(); // Arrête le deuxième moteur pour la synchronisation
	                }
	            });

	            Thread M1_Go_Thread = new Thread(new Runnable() {
	                @Override
	                public void run() {
	                    motor1.forward(); // Fait avancer le premier moteur
	                }
	            });

	            M2_Go_Thread.start();
	            M1_Go_Thread.start();

	            try {
	                M2_Go_Thread.join();
	                M1_Go_Thread.join();
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	            break;
           

	        case BACKWARD:
	            
	        	Thread M2_Back_Thread = new Thread(new Runnable() {
	                @Override
	                public void run() {
	                    motor2.backward(); // Arrête le deuxième moteur pour la synchronisation
	                }
	            });

	            Thread M1_Back_Thread = new Thread(new Runnable() {
	                @Override
	                public void run() {
	                    motor1.backward(); // Fait avancer le premier moteur
	                }
	            });

	            M2_Back_Thread.start();
	            M1_Back_Thread.start();

	            try {
	                M2_Back_Thread.join();
	                M1_Back_Thread.join();
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	            break;
	        case STOP:
	        	Thread M2_Stop_Thread = new Thread(new Runnable() {
	                @Override
	                public void run() {
	                    motor2.stop(); // Arrête le deuxième moteur pour la synchronisation
	                }
	            });

	            Thread M1_Stop_Thread = new Thread(new Runnable() {
	                @Override
	                public void run() {
	                    motor1.stop(); // Fait avancer le premier moteur
	                }
	            });

	            M2_Stop_Thread.start();
	            M1_Stop_Thread.start();

	            try {
	                M2_Stop_Thread.join();
	                M1_Stop_Thread.join();
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	            break;

	        case LEFT:
	            Thread M2_Left_Thread = new Thread(new Runnable() {
	                @Override
	                public void run() {
	                    motor2.stop(); // Arrête le deuxième moteur pour la synchronisation
	                }
	            });

	            Thread M1_Left_Thread = new Thread(new Runnable() {
	                @Override
	                public void run() {
	                    motor1.forward(); // Fait avancer le premier moteur
	                }
	            });

	            M2_Left_Thread.start();
	            M1_Left_Thread.start();

	            try {
	                M2_Left_Thread.join();
	                M1_Left_Thread.join();
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	            break;
	        case RIGHT:
	            Thread M2_Right_Thread = new Thread(new Runnable() {
	                @Override
	                public void run() {
	                    motor2.forward(); // Fait avancer le deuxième moteur
	                }
	            });

	            Thread M1_Right_Thread = new Thread(new Runnable() {
	                @Override
	                public void run() {
	                    motor1.stop(); // Arrête le premier moteur pour la synchronisation
	                }
	            });

	            M2_Right_Thread.start();
	            M1_Right_Thread.start();

	            try {
	                M2_Right_Thread.join();
	                M1_Right_Thread.join();
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	            break;


	        default:
	            break;
	    }
	}

}
