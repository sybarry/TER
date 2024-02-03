package pilottageMQTT;

import lejos.utility.Delay;

public class Main {

	public static void main(String[] args) throws Exception {
		// MQTT Connexion configuration
		final String MQTT_SERVER_IP = "192.168.127.124";
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