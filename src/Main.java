import java.net.UnknownHostException;
import java.util.UUID;

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
