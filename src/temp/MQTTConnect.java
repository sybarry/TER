package temp;

import lejos.utility.Delay;
import org.eclipse.paho.client.mqttv3.*;
import pilottageBluetoothMQTT.Controller;

import static pilottageBluetoothMQTT.MainClassPilottageMQTT_BT.*;

public class MQTTConnect {
    final String username = "ev3";
    final String password = "omelette";

    public MQTTConnect(String broker_IP, final String clientID, String topic) throws Exception {
        final String broker = "tcp://" + broker_IP + ":1883";

        MqttClient client = new MqttClient(broker, clientID);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        connOpts.setUserName(username);
        connOpts.setPassword(password.toCharArray());
//        connOpts.setKeepAliveInterval(1200);
//        connOpts.setAutomaticReconnect(true);

        print("Connecting to broker: " + broker);
        client.connect(connOpts);
        print("Connected");

        client.subscribe(topic);
        client.setCallback(new listenToMQTT());
    }

    private static class listenToMQTT implements MqttCallback {
        @Override
        public void connectionLost(Throwable cause) {
            print("MQTT Connexion lost:" + cause.getMessage());
            Delay.msDelay(2000);
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) {
            try {
                Controller ctrl = new Controller();
                String payload = new String(message.getPayload());
                switch (payload) {
                    case "go":
                        ctrl.movingForward();
                        break;
                    case "back":
                        ctrl.movingBackward();
                        break;
                    case "stop":
                        ctrl.stop();
                        break;
                    default:
                        print("Unknown: " + payload);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
        }
    }

}