package com.damao.context;

public class IpContext {
    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void setIp(String ip) {
        threadLocal.set(ip);
    }

    public static String getIp() {
        return threadLocal.get();
    }

    public static void removeIp() {
        threadLocal.remove();
    }
}
