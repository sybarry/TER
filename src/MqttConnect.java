import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.utility.Delay;

public class MqttConnect {
 
	
	public MqttConnect(String broker,String clientId,String topic) {
		
	      Synchro(broker,clientId,topic);
	}
	
	private void Synchro(String broker, final String clientId,String topic) {

        try {
            MqttClient client = new MqttClient(broker, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            LCD.drawString("Connecting to broker: " + broker, 0, 0);
            client.connect(connOpts);
            LCD.clear();
            LCD.drawString("Connected", 0, 0);
            Delay.msDelay(2000);
            LCD.clear();
 
         
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("Connection lost!");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                	   String payload = new String(message.getPayload());
                       if(payload.equals("go")) {
                      	 
                         MotorSync.startMotorsSync(Motor.B, Motor.C, Action.FORWARD, 2000);
                      } else if(payload.equals("back")) {
                      	
                      	MotorSync.startMotorsSync(Motor.B, Motor.C, Action.BACKWARD, 2000);
                      } else if(payload.equals(clientId)) {
                      
                      	MotorSync.startMotorsSync(Motor.B, Motor.B, Action.FORWARD, 5000); // Tourbillon
                      }
                     
                }
                

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Ne nécessaire pas pour la souscription
                }
            });

            
       
            client.subscribe(topic);

            String message = "Salut, de "+clientId;
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            client.publish(topic, mqttMessage);

            LCD.clear();
            LCD.drawString("Listening for messages", 0, 0);
            Delay.msDelay(2000);
            
           
            while (true) {
              Delay.msDelay(1000); 
              if(Button.DOWN.isDown()) {
            	  System.exit(0);
              }
            }
        } catch (MqttException e) {
            System.out.println("err:" + e.getMessage());
        }
    }
	
}

