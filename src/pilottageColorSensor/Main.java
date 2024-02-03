package pilottageColorSensor;
import lejos.utility.Delay;

public class Main {

	public static void main(String[] args) {

		ConduiteAutonome conduite = new ConduiteAutonome();
		try {
			conduite.test();
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
