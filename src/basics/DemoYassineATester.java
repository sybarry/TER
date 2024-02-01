package basics;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.utility.Delay;

public class DemoYassineATester {

	public static void main(String[] args) throws MqttException, IOException {
		// MQTT Connexion configuration 
		String MQTT_SERVER_IP = "192.168.0.188";
        String broker = "tcp://" + MQTT_SERVER_IP +":1883";
        final String clientId = "EV3_" + generateClientID();
        String topic = "ev3/topic";
        
        try {
			// Attempt to connect
			MqttClient client = new MqttClient(broker, clientId);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			print("Connecting to broker:" + MQTT_SERVER_IP);
			client.connect(connOpts);
			print("Connected.");
			Delay.msDelay(2000);
			
			// Subscribe to topic messages
			client.subscribe(topic);
			
			// Emit a message to topic (EV3_ followed by MAC Address)
			String message = "Connected: " + clientId;
			MqttMessage mqttMessage = new MqttMessage(message.getBytes());
			client.publish(topic, mqttMessage);
			print("Waiting action");
			
			// MQTT message listenner
			client.setCallback(new MqttCallback() {
				// Exit program if connexion is lost
			    @Override
			    public void connectionLost(Throwable cause) {
			    	print("Connection lost");
			    	Delay.msDelay(3000);
			    	System.exit(0);
			    }

			    // On message arrived from topic
			    @Override
			    public void messageArrived(String topic, MqttMessage message) {
			        String payload = new String(message.getPayload());
			         if(payload.equals("go")) {
			        	 print("Moving forward");
			            startMotorsSync(Motor.B, Motor.C, Action.FORWARD, 2000);
			        } else if(payload.equals("back")) {
			        	print("Moving backward");
			        	startMotorsSync(Motor.B, Motor.C, Action.BACKWARD, 2000);
			        } else if(payload.equals(clientId)) {
			        	print("Special move");
			        	startMotorsSync(Motor.B, Motor.B, Action.FORWARD, 5000); // Tourbillon
			        }
			         
			    }

			    @Override
			    public void deliveryComplete(IMqttDeliveryToken token) {
			    }
			});
		} catch (Exception e) {
			e.printStackTrace();
			print("\n\n\n" + e.getMessage());
		} 
       
        // Exit program by long pressing DOWN button 
        while (true) {
          Delay.msDelay(500); 
          if (Button.DOWN.isDown())
        	  System.exit(0);
        }
    }

	// Get the MAC Address of the Wifi dongle
	public static StringBuilder generateClientID() throws UnknownHostException, SocketException {
		InetAddress localHost = InetAddress.getLocalHost();
        NetworkInterface ni = NetworkInterface.getByInetAddress(localHost);
        byte[] mac = ni.getHardwareAddress();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
           stringBuilder.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
        }
		return stringBuilder;
	}
	
	public static void print(String message) {	    
		System.out.println(message);
	}

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
        
        Delay.msDelay(duration); // Moving duration
        // Stop the motors if the're moving
        if (action != Action.STOP)
        	startMotorsSync(M1, M2, Action.STOP, duration);
    }

	// Custom action on givedgi motor
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

    enum Action {
        FORWARD,
        BACKWARD,
        STOP,
        TOURBILLON
    }

}