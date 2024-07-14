package com.damao.utils;

import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;

@Slf4j
@Component
public class IpUtil {

    private static final String UNKNOWN = "unknown";
    private static Searcher searcher;


    IpUtil() throws FileNotFoundException {
        String dbPath = ResourceUtils.getFile("classpath:ip2region.xdb").toString();
        // 1、从 dbPath 加载整个 xdb 到内存。
        byte[] cBuff = new byte[0];
        try {
            cBuff = Searcher.loadContentFromFile(dbPath);
            log.info("成功加载ip2region.xdb文件");
        } catch (Exception e) {
            log.info("failed to load content from {}: {}\n", dbPath, e);
        }

        // 2、使用上述的 cBuff 创建一个完全基于内存的查询对象。
        try {
            searcher = Searcher.newWithBuffer(cBuff);
        } catch (Exception e) {
            log.info("failed to create content cached searcher: %s\n", e);
        }
    }

    /**
     * 获取 IP地址
     * 使用 Nginx等反向代理软件， 则不能通过 request.getRemoteAddr()获取 IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，
     * X-Forwarded-For中第一个非 unknown的有效IP字符串，则为真实IP地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    public static String getAddress(String ip) {
        try {
            return searcher.searchByStr(ip);
        } catch (Exception e) {
            log.info("failed to search({}): {}\n", ip, e);
        }
        return null;
    }

}