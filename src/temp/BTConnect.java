package temp;

import lejos.hardware.BrickFinder;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BTConnect {
    public static String MAC = BrickFinder.getLocal()
            .getBluetoothDevice().getBluetoothAddress();

    static DataInputStream in;
    static DataOutputStream out;

    public BTConnect() {
        System.out.println("Address MAC:" + MAC);

    }

    public static void connect() {
        BTConnector BTConnector = new BTConnector();
		BTConnection BTConnect = BTConnector.waitForConnection(0, NXTConnection.RAW);
		in = BTConnect.openDataInputStream();
		out = BTConnect.openDataOutputStream();
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
            System.out.println("BT Disconnection error: " + e.getMessage());
        }
    }
}
