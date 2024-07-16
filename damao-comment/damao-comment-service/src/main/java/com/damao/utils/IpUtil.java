package com.damao.utils;

import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
public class IpUtil {

    private static final String UNKNOWN = "unknown";
    private static Searcher searcher;


    IpUtil() throws IOException {
        // 使用ClassPathResource来获取资源文件
        Resource resource = new ClassPathResource("ip2region.xdb");
        try (InputStream inputStream = resource.getInputStream()) {
            // 读取文件到byte数组
            byte[] data = inputStream.readAllBytes();

            // 使用byte数组创建Searcher对象
            searcher = Searcher.newWithBuffer(data);
            log.info("成功读入ip2region.xdb");
        } catch (IOException e) {
            throw new RuntimeException("Unable to read ip2region.xdb", e);
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
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    public static String getAddress(String ip) {
        try {
            return searcher.searchByStr(ip);
        } catch (Exception e) {
            log.info("failed to search({}): {}\n", ip, e.toString());
        }
        return null;
    }

}