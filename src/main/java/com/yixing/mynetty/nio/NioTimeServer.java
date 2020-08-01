package com.yixing.mynetty.nio;

/**
 * 描述：
 *
 * @author 小谷
 * @Date 2020/5/11 15:33
 */
public class NioTimeServer {

    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (Exception e) {

            }
        }

        MultiplexerTimeServer multiplexerTimeServer = new MultiplexerTimeServer(port);

        new Thread(multiplexerTimeServer,"NIO-MultiplexerTimeServer-001").start();
    }
}
