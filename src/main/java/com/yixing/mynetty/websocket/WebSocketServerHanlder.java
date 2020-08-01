package com.yixing.mynetty.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;
import static io.netty.handler.codec.http.HttpUtil.setContentLength;

/**
 * 描述：
 *
 * @author 小谷
 * @Date 2020/5/12 15:45
 */
public class WebSocketServerHanlder extends SimpleChannelInboundHandler<Object> {

    private static final Logger log = Logger.getLogger(WebSocketServerHanlder.class.getName());

    private WebSocketServerHandshaker handshaker;


    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 传统的httpj接入
        if (msg instanceof FullHttpRequest) {
            handlerHttpRequest(ctx, (FullHttpRequest) msg);
        }
        // webSocket接入
        else if (msg instanceof WebSocketFrame) {
            handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private void handlerHttpRequest(ChannelHandlerContext ctx, FullHttpRequest msg) {
        // 如果http解码失败，返回http异常
        if (!msg.getDecoderResult().isSuccess()
                || (!"websocket".equals(msg.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, msg, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, BAD_REQUEST));
            return;
        }
        // 构造握手响应返回，本机测试
        WebSocketServerHandshakerFactory webSocketServerHandshakerFactory
                = new WebSocketServerHandshakerFactory("ws://localhost:8080/websocket",
                null, false);
        handshaker = webSocketServerHandshakerFactory.newHandshaker(msg);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), msg);
        }
    }


    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame msg) {
        // 判断是否关闭链路指令
        if (msg instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), ((CloseWebSocketFrame) msg).retain());
            return;
        }
        // 判断是否为Ping消息
        if (msg instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(msg.content().retain()));
        }
        // 本例程支持文本消息，不支持二进制消息
        if (!(msg instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format("%s frame types not supported", msg.getClass().getName()));
        }

        // 返回应答消息
        String request = ((TextWebSocketFrame) msg).text();
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("%s reveived %s", ctx.channel(), request));
        }
        ctx.channel().write(new TextWebSocketFrame(request + " ,欢迎使用Netty WebSocket 服务，现在时刻："
                + new Date().toString()));

    }

    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest msg, DefaultFullHttpResponse defaultFullHttpResponse) {
        // 返回应答给客户端
        if (defaultFullHttpResponse.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(defaultFullHttpResponse.getStatus().toString(), CharsetUtil.UTF_8);
            defaultFullHttpResponse.content().writeBytes(buf);
            buf.release();
            setContentLength(defaultFullHttpResponse, defaultFullHttpResponse.content().readableBytes());
        }

        // 如果是非Keep-Alive,关闭连接
        ChannelFuture channelFuture = ctx.channel().writeAndFlush(defaultFullHttpResponse);
        if (!isKeepAlive(msg) || defaultFullHttpResponse.getStatus().code() != 200) {
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
