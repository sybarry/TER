


import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.utility.Delay;

 

public class MotorSync {
	  
	

		// Move the motors at the same time with no delay (sync)
		public static void startMotorsSync(final NXTRegulatedMotor M1, final NXTRegulatedMotor M2, final Action action, final int duration) {
	        Thread M1_Thread = new Thread(new Runnable() {
	            @Override
	            public void run() {
	                performMotorAction(M1, action);
	            }
	        });

	        Thread M2_Thread = new Thread(new Runnable() {
	            @Override
	            public void run() {
	                performMotorAction(M2, action);
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
	        
	    //    Delay.msDelay(duration); // Moving duration
	        // Stop the motors if the're moving
	        if (action != Action.STOP)
	        	startMotorsSync(M1, M2, Action.STOP, duration);
	    }
		
	 
	   
	    private static void performMotorAction(NXTRegulatedMotor motor, Action action) {
	        switch (action) {
	            case FORWARD:
	                motor.forward();
	                break;
	            case BACKWARD:
	                motor.backward();
	                break;
	            case STOP:
	                motor.stop();
	                break;
			default:
				break;
	        }
	    }
}
