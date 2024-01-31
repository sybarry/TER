import java.awt.Color;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.*;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.utility.Delay;

public class Main {

    public static void main(String[] args) throws UnknownHostException{
        // Replace with your PC's IP address
        String broker = "tcp://172.20.10.4:1883";
    
       final String clientId = generateClientId();
        String topic = "ev3/topic";
         
            MqttConnect op = new MqttConnect(broker, clientId, topic); 
    	 
      
      
 
}
   
    public static String generateClientId() {
        // Utilisez UUID pour générer un identifiant unique
        return "EV3_" + UUID.randomUUID().toString();
    } 
}
