package com.yixing.mynetty.serializable.java;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * 描述：
 *
 * @author 小谷
 * @Date 2020/5/11 23:12
 */
public class JavaUser implements Serializable {
    private static final long serialVersionUID = 8882229924945813170L;

    private String userName;

    private int userId;

    public JavaUser buildUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public JavaUser buildUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public byte[] codeC() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        byte[] value = this.userName.getBytes();
        buffer.putInt(value.length);
        buffer.put(value);
        buffer.putInt(this.userId);
        buffer.flip();
        value = null;
        byte[] result = new byte[buffer.remaining()];
        buffer.get(result);
        return result;
    }
}
