package com.yixing.mynetty.filechannel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 描述：
 *
 * @author 小谷
 * @Date 2020/5/13 9:47
 */
public class FileChannelUser {

    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("D:/公钥.txt","rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        String content = "18684591929|湖南省耒阳市";
        ByteBuffer writeBuffer = ByteBuffer.allocate(128);
        writeBuffer.put(content.getBytes());
        writeBuffer.flip();
        fileChannel.write(writeBuffer);
    }
}
