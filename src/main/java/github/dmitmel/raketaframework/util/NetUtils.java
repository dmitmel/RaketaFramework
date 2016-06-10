package github.dmitmel.raketaframework.util;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class NetUtils {
    public static String inetSocketAddressToString(InetSocketAddress inetSocketAddress) {
        return String.format("%s:%d", inetSocketAddress.getHostString(), inetSocketAddress.getPort());
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
            throw github.dmitmel.raketaframework.util.exceptions.SocketException.extractFrom(e);
        }

        throw new CurrentIpCannotBeFoundException();
    }

    public static boolean isAddressSiteLocal(InetAddress address) {
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
