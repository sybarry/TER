package pilottageMQTT;

import lejos.utility.Delay;

public class MainClassPilottageMQTT {

	public static void main(String[] args) throws Exception {
		// MQTT Connexion configuration
		final String MQTT_SERVER_IP = "172.20.10.4";
		final String clientId = "EV3_" + Utils.generateClientID();
		final String topic = "ev3/topic";

		try {
			new MQTTConnect(MQTT_SERVER_IP, clientId, topic);
		} catch (InterruptedException e) {
			Utils.print("\n\n\n" + e.getMessage());
		} catch (Exception e) {
			Utils.print("\n\n\n" + e.getMessage());
		} finally {
			Delay.msDelay(2000);
			System.exit(0);
		}
	}

}
	
