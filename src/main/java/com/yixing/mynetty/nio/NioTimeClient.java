package com.yixing.mynetty.nio;

/**
 * 描述：
 *
 * @author 小谷
 * @Date 2020/5/11 16:20
 */
public class NioTimeClient {

    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (Exception e) {

            }
        }

        new Thread(new NioTimeClientHandler("127.0.0.1", port), "TimeClient-001").start();
    }
}
