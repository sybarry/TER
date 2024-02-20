package pilottageBluetoothMQTT;

import org.eclipse.paho.client.mqttv3.*;
import lejos.utility.Delay;
import static pilottageBluetoothMQTT.MainClassPilottageMQTT_BT.*;

public class MQTTConnect {

    public MQTTConnect(String broker_IP, final String clientID, String topic) throws Exception {
        final String username = "ev3";
        final String password = "omelette";
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
        final Controller ctrl = new Controller();
        final Thread[] BTThread = new Thread[1];
        connect();

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                print("MQTT Connexion lost:" + cause.getMessage());
                Delay.msDelay(2000);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                try {
                    String payload = new String(message.getPayload());
                    switch (payload) {
                        case "go":
                            ctrl.movingForward();
                            Delay.msDelay(2000);
                            ctrl.stop();
                            break;
                        case "back":
                            ctrl.movingBackward();
                            Delay.msDelay(2000);
                            ctrl.stop();
                            break;
                        default:
                            print("Unknown: " + payload);
                            break;
                        case "release":
                            pauseBT = false;
                            if (!BTThread[0].isAlive()) {
                                BTThread[0] = listenToBT(ctrl);
                                BTThread[0].start();
                            } else synchronized (BTThread[0]) {
                                if (BTConnect == null)
                                    connect();
                                BTThread[0].notify();
                                }
                            print("Connected to BT");
                            break;
                        case "control":
                            pauseBT = true;
                            disconnect();
                            print("Disconnected from BT");
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
            }
        });

    }
}