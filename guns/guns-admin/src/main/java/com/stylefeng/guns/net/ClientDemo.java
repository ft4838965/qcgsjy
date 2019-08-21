package com.stylefeng.guns.net;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class ClientDemo {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1",9999);
            Boolean flag = true;
            while (flag){
                Scanner input = new Scanner(System.in);
                System.out.println("请输入一个字符串(中间能加空格或符号)");
                String a = input.nextLine();
                String strMessage = "client: "+a;

                System.out.println(strMessage);
                socket.getOutputStream().write(strMessage.getBytes());
                socket.shutdownOutput();

                BufferedReader buffRead = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String str = buffRead.readLine();
                System.out.println(str);
                buffRead.close();
                if ("over".equals(str)){
                    socket.close();
                    System.out.println("socket.close()");
                }
            }

        }catch (Exception e){
            e.printStackTrace();

        }

    }

}
