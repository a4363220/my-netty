package com.yixing.mynetty.udp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.ThreadLocalRandom;

import java.net.DatagramPacket;

/**
 * 描述：
 *
 * @author 小谷
 * @Date 2020/5/13 9:20
 */
public class ChineseProverbServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    // 谚语列表
    private static final String[] DICTIONARY = {"三十年功名尘与土", "八千里路云和月", "莫等闲", "白了少年头", "空悲切"};

    private String nextQuote() {
        int quteId = ThreadLocalRandom.current().nextInt(DICTIONARY.length);
        return DICTIONARY[quteId];
    }

    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        byte[] req = msg.getData();
        System.out.println(req.length);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
