package com.yixing.mynetty.serializable.java;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * 描述：
 *    jdk serializable 字节码长度大，效率慢
 *    二进制 字节码长度小，效率快
 *
 * @author 小谷
 * @Date 2020/5/11 23:42
 */
public class TestJavaUser {

    public static void main(String[] args) throws IOException {
        JavaUser javaUser = new JavaUser();
        javaUser.buildUserId(100).buildUserName("Welcome to Netty");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(javaUser);
        os.flush();
        os.close();
        byte[] b = bos.toByteArray();
        System.out.println("The jdk serializable lengt is " + b.length);
        bos.close();
        System.out.println("The byte array serializable " + javaUser.codeC().length);
    }
}
