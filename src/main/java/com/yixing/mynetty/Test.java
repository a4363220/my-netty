package com.yixing.mynetty;

/**
 * 描述：
 *
 * @author 小谷
 * @Date 2020/5/14 15:14
 */
public class Test {

    public static void main(String[] args) {

        int a = Integer.MAX_VALUE;
        int b = Integer.MIN_VALUE;
        System.out.println("a："+a);
        System.out.println("b："+b);
        int c = b-1;
        System.out.println("b-1："+c);
        int d = b+1;
        System.out.println("b+1："+d);
        if(a+1<a){
            System.out.println("存在i+1<i的数");
        }if(b-1>b){
            System.out.println("存在i-1>i的数");
        }
    }
}
