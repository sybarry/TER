import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.UUID;

import javax.sound.sampled.Port;

import org.eclipse.paho.client.mqttv3.*;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.remote.ev3.RMISampleProvider;
import lejos.remote.ev3.RemoteEV3;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Main {

    public static void main(String[] args) throws UnknownHostException{
     /*   // Replace with your PC's IP address
        String broker = "tcp://172.20.10.4:1883";
    
       final String clientId = generateClientId();
        String topic = "ev3/topic";
         
            MqttConnect op = new MqttConnect(broker, clientId, topic);*/
    	   ConduiteAutonome conduite  =new ConduiteAutonome();
    	   conduite.test();
		
		
	
	}
 

  
    
  
  

    public static String generateClientId() {
        // Utilisez UUID pour générer un identifiant unique
        return "EV3_" + UUID.randomUUID().toString();
    } 
}
