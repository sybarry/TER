package temp;

import lejos.hardware.Button;
import pilottageBluetoothMQTT.MQTTConnect;
import static temp.BTConnect.MAC;

public class MainClassMQTT_BT {
//    static final String MQTT_SERVER_IP = "141.145.203.36";
    static final String MQTT_SERVER_IP = "192.168.0.188";
    static final String clientId = "EV3_" + MAC;

    public static void main(String[] args) {
        try {
            new MQTTConnect(MQTT_SERVER_IP, clientId, "ev3/topic");

        } catch (Exception e) {
            System.out.println("MQTT Connection error: " + e.getMessage());
        }

        while (true) {
            if (Button.DOWN.isDown()) {
                System.out.println("Exiting...");
                System.exit(1);
            }
        }

    }

    


}
