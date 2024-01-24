import java.net.UnknownHostException;
import java.util.UUID;

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

public class Demo {

	public static void main(String[] args) throws UnknownHostException {
        String broker = "tcp://192.168.0.188:1883";
        
 
        final String clientId = generateClientID();
        String topic = "ev3/topic";
        
        while (clientId == null) {
            try {
                Thread.sleep(150); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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
                        faireAvancer(2000);
                    }else if(payload.equals("back")) {
                    	faireReculer(2000);
                    }else if(payload.equals(clientId)) {
                    	faireAvancer(2000);
                    }
                     
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                }
            });

            
       
            client.subscribe(topic);

            String message = "Salut, de " + clientId;
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            client.publish(topic, mqttMessage);

            LCD.clear();
           System.out.println("Client ID: "+clientId);
            Delay.msDelay(2000);
            
           
            while (true) {
              Delay.msDelay(1000); 
              if (Button.DOWN.isDown())
            	  System.exit(0);
            }
            
            
        } catch (MqttException e) {
            System.out.println("err:" + e.getMessage());
        }
    }

    public static void faireAvancer(int duree) {
        Motor.B.forward();
        Motor.C.forward(); 

        Delay.msDelay(duree); 

        Thread leftMotorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Motor.B.stop();
            }
        });

        Thread rightMotorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Motor.C.stop();
            }
        });

        leftMotorThread.start();
        rightMotorThread.start();

        // Wait for both threads to finish
        try {
            leftMotorThread.join();
            rightMotorThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void faireReculer(int duree) {
        Motor.B.backward(); // Moteur gauche en arrière
        Motor.C.backward(); // Moteur droit en arrière

        Delay.msDelay(duree); // Pause pendant la durée spécifiée

        Motor.B.stop(); // Arrête le moteur gauche
        Motor.C.stop(); // Arrête le moteur droit
        
    }
    public static String generateClientID() {
    	    	
    	return "EV3_" + UUID.randomUUID().toString();
    }


}