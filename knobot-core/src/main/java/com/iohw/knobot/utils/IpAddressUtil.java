package com.iohw.knobot.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.net.*;
import java.util.Enumeration;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * IP地址工具类
 */
public class IpAddressUtil {

    /**
     * 获取本机所有IP地址（包括IPv4和IPv6）
     * @return 本机IP地址列表
     */
    public static String[] getLocalIPs() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            StringBuilder sb = new StringBuilder();
            
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // 过滤掉回环接口和未启动的接口
                if (iface.isLoopback() || !iface.isUp()) {
                    continue;
                }
                
                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    // 过滤掉IPv6地址和回环地址
                    if (!addr.isLoopbackAddress() && addr instanceof Inet4Address) {
                        sb.append(addr.getHostAddress()).append(",");
                    }
                }
            }
            
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 1); // 移除最后一个逗号
                return sb.toString().split(",");
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return new String[0];
    }

    /**
     * 获取本机第一个非回环IP地址
     * @return IP地址字符串
     */
    public static String getLocalIP() {
        String[] ips = getLocalIPs();
        return ips.length > 0 ? ips[0] : "127.0.0.1";
    }

    /**
     * 获取公网IP地址（通过第三方服务查询）
     * @return 公网IP地址
     */
    public static String getPublicIP() {
        String[] services = {
            "https://api.ipify.org",
            "https://checkip.amazonaws.com",
            "https://ipinfo.io/ip",
            "https://ifconfig.me/ip"
        };
        
        for (String service : services) {
            try {
                URL url = new URL(service);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(3000);
                conn.setReadTimeout(3000);
                
                try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                    String ip = reader.readLine().trim();
                    if (ip != null && !ip.isEmpty()) {
                        return ip;
                    }
                }
            } catch (Exception e) {
                // 如果某个服务失败，尝试下一个
                continue;
            }
        }
        return null;
    }

    /**
     * 从HttpServletRequest获取客户端IP地址
     * @param request HttpServletRequest对象
     * @return 客户端IP地址
     */
    public static String getClientIP(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        
        // 对于通过多个代理的情况，第一个IP为客户端真实IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    /**
     * 检查IP地址是否在指定网段内
     * @param ipAddress 要检查的IP地址
     * @param subnet 网段，如 "192.168.1.0/24"
     * @return 是否在网段内
     */
    public static boolean isInSubnet(String ipAddress, String subnet) {
        try {
            String[] parts = subnet.split("/");
            String network = parts[0];
            int prefix;
            
            if (parts.length < 2) {
                prefix = 32;
            } else {
                prefix = Integer.parseInt(parts[1]);
            }
            
            InetAddress ip = InetAddress.getByName(ipAddress);
            InetAddress net = InetAddress.getByName(network);
            
            byte[] ipBytes = ip.getAddress();
            byte[] netBytes = net.getAddress();
            
            // 检查IPv4
            if (ipBytes.length == 4 && netBytes.length == 4) {
                int mask = 0xffffffff << (32 - prefix);
                int ipInt = ((ipBytes[0] & 0xFF) << 24) |
                             ((ipBytes[1] & 0xFF) << 16) |
                             ((ipBytes[2] & 0xFF) << 8) |
                             (ipBytes[3] & 0xFF);
                int netInt = ((netBytes[0] & 0xFF) << 24) |
                             ((netBytes[1] & 0xFF) << 16) |
                             ((netBytes[2] & 0xFF) << 8) |
                             (netBytes[3] & 0xFF);
                
                return (ipInt & mask) == (netInt & mask);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}