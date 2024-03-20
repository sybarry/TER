package pilottageMQTT;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.utility.Delay;
import pilottageColorSensor.Automate;
import pilottageColorSensor.ConduiteAutonome;
import pilottageColorSensor.Direction;

public class MQTTConnect {
	ConduiteAutonome  conduiteAutonome = null;

	public MQTTConnect(String broker_IP, final String clientID, String topic) throws Exception {
		// Attempt to connect
		final String broker = "tcp://" + broker_IP + ":1883";
		MqttClient client = new MqttClient(broker, clientID);
		MqttConnectOptions connOpts = new MqttConnectOptions();
		 
		connOpts.setCleanSession(true);
		// Security credentials; not available in the broker for the moment
		// connOpts.setUserName("");
		// connOpts.setPassword("");
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
			public void messageArrived(String topic, MqttMessage message) throws ParseException, InterruptedException, RuntimeException {
				String payload = new String(message.getPayload());
				
				if(payload.contains(String.valueOf('('))) {
					  //On recupere le fichier Json depuis MQTT et on Construit L'automate 
					Automate auto = new Automate(payload);
					conduiteAutonome = new ConduiteAutonome(auto);
					
				}else {
					if(conduiteAutonome !=null) {
						conduiteAutonome.execute(payload);
					}else {
						Utils.print("Veuillez lancer l'automate en premier lieu");
					}
						
					
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
