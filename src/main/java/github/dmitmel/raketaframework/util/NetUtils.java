package github.dmitmel.raketaframework.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class NetUtils {
    private NetUtils() {
        throw new RuntimeException("Can\'t create instance of NetUtils");
    }
    
    
    public static InetAddress getCurrentSiteLocalIP() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();

                if (networkInterface.isUp() && !networkInterface.isLoopback() && !networkInterface.isPointToPoint()
                        && !networkInterface.isVirtual()) {
                    Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();

                    while (addresses.hasMoreElements()) {
                        InetAddress address = addresses.nextElement();
                        if (isAddressSiteLocal(address))
                            return address;
                    }
                }
            }
        } catch (java.net.SocketException e) {
            throw github.dmitmel.raketaframework.exceptions.SocketException.extractFrom(e);
        }

        throw new CannotGetSiteLocalIPException();
    }

    private static boolean isAddressSiteLocal(InetAddress address) {
        return  !address.isAnyLocalAddress() &&
                !address.isLinkLocalAddress() &&
                !address.isLoopbackAddress() &&
                !address.isMCGlobal() &&
                !address.isMCLinkLocal() &&
                !address.isMCNodeLocal() &&
                !address.isMCOrgLocal() &&
                !address.isMCSiteLocal() &&
                !address.isMulticastAddress() &&
                address.isSiteLocalAddress();
    }
}
