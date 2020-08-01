package com.yixing.mynetty.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;

/**
 * 描述：
 * bio服务端
 * <p>
 * 每有一个新的客户端请求接入，服务端必须创建一个新的线程处理新接入的客户端链路
 * 一个线程只能处理一个客户端连接，在高性能服务器应用领域，往往需要面向成千上万个客户端
 * 并发连接，这种模型无法满足高性能，高并发接入的场景
 * <p>
 * 使用线程池，伪io异步
 *
 * @author 小谷
 * @Date 2020/5/11 14:45
 */
public class TimeServer {

    public static void main(String[] args) throws IOException {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (Exception e) {

            }
        }

        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("The time server is start in port : " + port);
            Socket socket = null;

            TimeServerHandlerExecutePool singleExecute =
                    new TimeServerHandlerExecutePool(50,10000);// 创建I/O任务线程池


            while (true) {
                socket = server.accept();
                //new Thread(new TimeServerHandler(socket)).start();
                // 伪io异步
                singleExecute.execute(new TimeServerHandler(socket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                System.out.println("The time server close");
                server.close();
                server = null;
            }
        }
    }
}
