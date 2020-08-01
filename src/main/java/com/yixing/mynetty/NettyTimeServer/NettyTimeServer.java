package com.yixing.mynetty.NettyTimeServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;

/**
 * 描述：
 *
 * @author 小谷
 * @Date 2020/5/11 17:32
 */
public class NettyTimeServer {

    public void bind(int port) throws Exception {
        // 配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChildChannelHandler());
            // 绑定端口，同步等待成功
            ChannelFuture future = serverBootstrap.bind(port).sync();
            // 等待服务端监听端口关闭
            future.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放线程资源
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        protected void initChannel(SocketChannel ch) throws Exception {
            /**
             * LineBasedFrameDecoder:
             *    工作原理，是它依次遍历ByteBuf中的可读字节，判断看是否有 \n 或者 \r\n
             *    如果有以此位置为结束位置，从可读索引到结束位置区间的字节就组成了一行。
             *    它是以换行符为结束标志的解码器，支持携带结束符或者不携带结束符两种解码方式
             *    同时支持单行的最大长度，如果连续读取到最大长度后仍然没有发现换行符就会抛出异常
             * StringDecoder:
             *    将接收到的对象转换成字符串，然后继续调用后面的handler
             *    两者相结合就是按行切换的文本解码器，用来支持粘包和拆包
             *
             * */
            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
            ch.pipeline().addLast(new StringDecoder());
            /**
             * DelimiterBasedFrameDecoder:
             *    自定义分隔符为解码器，并设置最大长度，超过此长度没有发现$_分隔符则抛出异常
             * */
//            ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
//            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,delimiter));

            /**
             * FixedLengthFrameDecoder:
             *    固定长度解码器，无论一次接收多少数据包，它都会按照构造函数中设置的固定长度进行编码
             *    ，如果是半包消息，FixedLengthFrameDecoder会缓存半包消息并等待下个包到达后进行拼包，直到读取到一个完整的包
             * */
            //ch.pipeline().addLast(new FixedLengthFrameDecoder(20));

            /**
             * protobuf编解码
             * ProtobufVarint32FrameDecoder：半包处理
             * ProtobufEncoder：解码
             * */
            //ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
            // 这里还需要携带目标类
            //ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
            //ch.pipeline().addLast(new ProtobufEncoder());

            ch.pipeline().addLast(new NettyTimeServerHandler());
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (Exception e) {

            }
        }

        new NettyTimeServer().bind(port);

    }
}
