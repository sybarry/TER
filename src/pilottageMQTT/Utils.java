package pilottageMQTT;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class Utils {
	
		// Get the MAC Address of the Wifi dongle
		public static StringBuilder generateClientID() throws Exception {
			InetAddress localHost = getLocalIPAddress();
	        NetworkInterface ni = NetworkInterface.getByInetAddress(localHost);
	        byte[] mac = ni.getHardwareAddress();
	        StringBuilder stringBuilder = new StringBuilder();
	        for (int i = 0; i < mac.length; i++) {
	           stringBuilder.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
	        }
			return stringBuilder;
		}
		
		// Get the IP Address of the EV3 robot
		private static InetAddress getLocalIPAddress() throws Exception {
	        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

	        while (interfaces.hasMoreElements()) {
	            NetworkInterface iface = interfaces.nextElement();

	            if (iface.isUp() && !iface.isLoopback() && !iface.isVirtual()) {
	                Enumeration<InetAddress> addresses = iface.getInetAddresses();

	                while (addresses.hasMoreElements()) {
	                    InetAddress addr = addresses.nextElement();

	                    if (addr.getHostAddress().matches("\\d+\\.\\d+\\.\\d+\\.\\d+") 
	                    		&& !addr.isLoopbackAddress()) {
	                        return addr;
	                    }
	                }
	            }
	        }

	        throw new Exception("No suitable network interface found");
	    }
		
		public static void print(String message) {	    
			System.out.println(message);
		}
}
