package com.yixing.mynetty.serializable.protol;

/**
 * 描述：
 *
 * @author 小谷
 * @Date 2020/5/12 9:36
 */
public class ProtoTest {

    public static void main(String[] args) {
         JavaUserProto.JavaUser javaUser = JavaUserProto.JavaUser.newBuilder()
                 .setUserId(100).setUserName("Welcome to Netty").build();
        System.out.println(javaUser.getUserId());
        System.out.println(javaUser.getUserName());

        byte[] bytes = javaUser.toByteArray();
        System.out.println("字节长度："+bytes.length);
        System.out.println("tostring："+javaUser.toString());
        System.out.println("toByteString："+javaUser.toByteString());
    }
}
