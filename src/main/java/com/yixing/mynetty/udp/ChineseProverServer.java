package com.yixing.mynetty.udp;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;


/**
 * 描述：
 *
 * @author 小谷
 * @Date 2020/5/13 9:11
 */
public class ChineseProverServer {

    public void run(int port)throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST,true)
                    .handler(new ChineseProverbServerHandler());

            bootstrap.bind(port).sync().channel().closeFuture().await();

        }finally {
           group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if(args.length>0){
           try{
              port = Integer.parseInt(args[0]);
           }catch (NumberFormatException e){
               e.printStackTrace();
           }
        }

        new ChineseProverServer().run(port);
    }
}
