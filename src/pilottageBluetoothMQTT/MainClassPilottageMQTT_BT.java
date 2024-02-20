package pilottageBluetoothMQTT;

import lejos.hardware.Bluetooth;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.LocalBTDevice;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;
import lejos.utility.Delay;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MainClassPilottageMQTT_BT {
	public static boolean pauseBT;
	public static DataOutputStream out;
    public static DataInputStream in;
    public static BTConnection BTConnect;

    public static void main(String[] args) {
        LocalBTDevice localBTDevice = BrickFinder.getLocal().getBluetoothDevice();
        String MAC = localBTDevice.getBluetoothAddress();
        print("Address MAC:" + MAC);

		final String MQTT_SERVER_IP = "141.145.203.36";
        //final String MQTT_SERVER_IP = "192.168.0.188";
        final String clientId = "EV3_" + MAC;

        try {
            new MQTTConnect(MQTT_SERVER_IP, clientId, "ev3/topic");
        } catch (Exception e) {
            print("\n\n\n\n" + e.getMessage());
        }

        while (true) {
            if (Button.DOWN.isDown()) {
                print("Program terminated");
                Delay.msDelay(1500);
                System.exit(1);
            }
        }

    }

    public static Thread listenToBT(final Controller ctrl) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
					synchronized (this) {
						while (pauseBT) {
							try {
								wait();
							} catch (InterruptedException e) {
								Thread.currentThread().interrupt();
								print("Thread was interrupted: " + e.getMessage());
							}
						}
					}
                    if (Button.DOWN.isDown())
                        System.exit(1);

                    try {
                        int commande = in.readByte();
                        if (pauseBT || commande == 10) {
                            disconnect();
                            break;
                        }
                        switch (commande) {
                            case 1:
                                ctrl.movingForward();
                                break;
                            case 2:
                                ctrl.movingBackward();
                                break;
                            case 3:
                                ctrl.turnLeft(2);
                                break;
                            case 4:
                                ctrl.turnRight(2);
                                break;
                            case 5:
                                ctrl.accelerate(50);
                                break;
                            case 6:
                                ctrl.decelerated(50);
                                break;
                            case 7:
                                ctrl.stop();
                                break;
                        }
                    } catch (IOException ioe) {
                        print("IO Exception readInt: " + ioe.getMessage());
						break;
                    }
                }
            }
        });
    }

    static void connect() {
        BTConnector BTconnector = (BTConnector) Bluetooth.getNXTCommConnector();
        BTConnect = BTconnector.waitForConnection(10000, NXTConnection.RAW);
        out = BTConnect.openDataOutputStream();
        in = BTConnect.openDataInputStream();
        print("Waiting for BT App");
    }

    public static void disconnect() {
        try {
            if (out != null)
                out.close();
            if (in != null)
                in.close();
//            if (BTConnect != null)
//                BTConnect.close();
        } catch (IOException e) {
            print("Bluetooth disconnection error: " + e.getMessage());
        }
    }

    public static void print(String message) {
        System.out.println(message);
    }

}
