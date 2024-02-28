package pillotageBluetoothMQTT;

import org.eclipse.paho.client.mqttv3.*;

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
        
        client.connect(connOpts);
        System.out.println("Connected to broker: " + broker);

        client.subscribe(topic);
        client.setCallback(new listenToMQTT());
        
        while (true)
        	continue;
              
    }

    private static class listenToMQTT implements MqttCallback {
        @Override
        public void connectionLost(Throwable cause) {
            System.out.println("MQTT Connection lost: " + cause.getMessage());
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) {
            try {
                String payload = new String(message.getPayload());
                switch (payload) {
                    case "go":
                        MainMQTT_BT.ctrl.movingForward();
                        break;
                    case "back":
                    	MainMQTT_BT.ctrl.movingBackward();
                        break;
                    case "stop":
                    	MainMQTT_BT.ctrl.stop();
                        break;
                    case "disconnect":
                    	MainMQTT_BT.BT_disconnected = true;
                    	break;
                    case "connect":
                    	MainMQTT_BT.BT_disconnected = false;
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