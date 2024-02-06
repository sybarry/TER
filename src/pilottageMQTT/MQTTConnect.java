package pilottageMQTT;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.utility.Delay;

public class MQTTConnect {

	public MQTTConnect(String broker_IP, final String clientID, String topic) throws Exception {
		final String username = "ev3";
		final String password = "omelette";
		
		
		// Attempt to connect
		final String broker = "tcp://" + broker_IP + ":1883";
		MqttClient client = new MqttClient(broker, clientID);
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		connOpts.setUserName(username);
		connOpts.setPassword(password.toCharArray());
		
		Utils.print("Connecting to broker " + broker_IP);
		client.connect(connOpts);
		Utils.print("Connected");
		Delay.msDelay(1500);

		// Subscribe to topic messages
		client.subscribe(topic);

		// Emit a message to topic (EV3_ followed by MAC Address)
		String message = "Connected: " + clientID;
		MqttMessage mqttMessage = new MqttMessage(message.getBytes());
		client.publish(topic, mqttMessage);
		Utils.print("Waiting action");

		// MQTT message listenner
		client.setCallback(new MqttCallback() {

			// Exit program if connexion is lost
			@Override
			public void connectionLost(Throwable cause) {
				Utils.print("Connexion lost");
				Delay.msDelay(2000);
				return;
			}

			// On message arrived from topic
			@Override
			public void messageArrived(String topic, MqttMessage message) {
				String payload = new String(message.getPayload());
				if (payload.equals("go")) {
					Utils.print("Moving forward");
					MotorSync.startMotorsSync(Motor.B, Motor.C, Action.FORWARD, 2000);
				} else if (payload.equals("back")) {
					Utils.print("Moving backward");
					MotorSync.startMotorsSync(Motor.B, Motor.C, Action.BACKWARD, 2000);
				} else if (payload.equals(clientID)) {
					Utils.print("Special move");
					MotorSync.startMotorsSync(Motor.B, Motor.B, Action.FORWARD, 5000); // Tourbillon
				}
			}

			@Override
			public void deliveryComplete(IMqttDeliveryToken token) {
			}
		});

		// Exit program by long-pressing DOWN button
		while (true) {
			Delay.msDelay(500);
			if (Button.DOWN.isDown())
				throw new InterruptedException("Program stopped by user");
		}

	}

}
