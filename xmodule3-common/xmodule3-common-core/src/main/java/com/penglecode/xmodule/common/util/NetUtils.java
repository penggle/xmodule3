package com.penglecode.xmodule.common.util;

import org.springframework.util.Assert;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 网络工具类
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/4/10 10:34
 */
public class NetUtils {

    private NetUtils() {}

    /**
     * 类似于ping命令检测指定的host主机是否网络通达
     *
     * @param host              - 主机名
     * @param timeoutOfMillis   - 超时毫秒数
     * @return
     */
    public static boolean ping(String host, int timeoutOfMillis) {
        Assert.hasText(host, "Parameter 'host' must be required!");
        Assert.isTrue(timeoutOfMillis > 0, "Parameter 'timeoutOfMillis' must be > 0!");
        try {
            return InetAddress.getByName(host).isReachable(timeoutOfMillis);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 简易的使用Socket发送数据
     *
     * @param host      - 主机名
     * @param port      - 端口号
     * @param blocking  - 是否阻塞
     * @param data      - 发送数据
     */
    public static void netCat(String host, int port, boolean blocking, ByteBuffer data) throws IOException {
        Assert.hasText(host, "Parameter 'host' must be required!");
        Assert.isTrue(port > 0 && port < 65535, "Parameter 'port' must be in range(0, 65535)!");
        try (SocketChannel channel = SocketChannel.open(new InetSocketAddress(host, port))) {
            channel.configureBlocking(blocking);
            channel.write(data);
        }
    }

    /**
     * 使用普通Socket发送数据
     *
     * @param host      - 主机名
     * @param port      - 端口号
     * @param data      - 发送数据
     */
    public static void netCat(String host, int port, byte[] data) throws IOException {
        try (Socket socket = new Socket(host, port); OutputStream out = socket.getOutputStream()) {
            out.write(data);
            out.flush();
        }
    }

}
