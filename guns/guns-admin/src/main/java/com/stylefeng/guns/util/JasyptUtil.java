package com.stylefeng.guns.util;

import org.jasypt.util.text.BasicTextEncryptor;

/**
 * Created by Heyifan Cotter on 2019/4/29.
 */
public class JasyptUtil {

    public static String getENC(String salt,String password){
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        //加密所需的salt(盐)
        textEncryptor.setPassword(salt);
        //要加密的数据（密码）
        String passwordENC = textEncryptor.encrypt(password);
        return passwordENC;
    }

    public static String changeENC(String salt,String passwordENC){
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        //加密所需的salt(盐)
        textEncryptor.setPassword(salt);
        //要加密的数据（密码）
        String password = textEncryptor.decrypt(passwordENC);
        return password;
    }
    public static void main(String[] args) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        //加密所需的salt(盐)
        textEncryptor.setPassword("G0CvDz7oJn6");
        //要加密的数据（密码）
        String password = textEncryptor.encrypt("root");
        String decrypt = textEncryptor.decrypt(password);
        System.out.println("password（密文）:"+password);
        System.out.println("password（原文）:"+decrypt);
    }
}
