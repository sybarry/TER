package pilottageMQTT;

import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.utility.Delay;

public class MotorSync {

	// Move the motors at the same time with no delay (sync)
	public static void startMotorsSync(final NXTRegulatedMotor M1, final NXTRegulatedMotor M2, final Action action,
			final int duration) {
		
		   Thread M1_Thread = new Thread(new Runnable() {
	            @Override
	            public void run() {
	                performMotorAction(M1,M2, action);
	            }
	        });
		   
		   Thread M2_Thread = new Thread(new Runnable() {
	            @Override
	            public void run() {
	                performMotorAction(M2,M1, action);
	            }
	        });

		   M1_Thread.start();
	        M2_Thread.start();

	        try {
	            M1_Thread.join();
	            M2_Thread.join();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }

	        
	
		
		

		
		
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
	        	motor1.forward();
                break;

	        case BACKWARD:
	            motor1.backward();
                break;
	        case STOP:
	            motor1.stop();
	            motor2.stop();
	            break;

	        case LEFT:
	            Thread M2_Left_Thread = new Thread(new Runnable() {
	                @Override
	                public void run() {
	                    motor2.stop();
	                }
	            });

	            Thread M1_Left_Thread = new Thread(new Runnable() {
	                @Override
	                public void run() {
	                    motor1.forward();
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
	                    motor1.stop();
	                }
	            });

	            Thread M1_Right_Thread = new Thread(new Runnable() {
	                @Override
	                public void run() {
	                    motor2.forward();
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
