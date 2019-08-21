package com.stylefeng.guns.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Heyifan Cotter on 2019/4/15.
 */
public class ServerDemo {
   public static void main(String[] args) {
       try {
           ServerSocket serverSocket = new ServerSocket(9999);
           boolean flag = true;
           int i =0;
           BufferedReader buffRead = null;
           Socket socket = null;
           System.out.println("服务器启动!");
           while(flag){
               socket = serverSocket.accept();
               if (i == 0){
//                   buffRead = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                   String str = buffRead.readLine();
//                   System.out.println(str);
//                   socket.shutdownInput();
                   socket.getOutputStream().write(("连接成功！").getBytes());
                   i++;
                   socket.close();
               }else {
                   buffRead = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                   String str = buffRead.readLine();
                   System.out.println(str);
                   socket.shutdownInput();

                   System.out.println("请输入一个字符串(中间能加空格或符号)");
                   Scanner input = new Scanner(System.in);
                   String a = input.nextLine();
                   socket.getOutputStream().write(("server: "+a).getBytes());
                   socket.close();
                   System.out.println("socket.close()");

                   buffRead.close();
               }
           }

           serverSocket.close();
           System.out.println("serverSocket close!");
       }catch (Exception e){
           e.printStackTrace();
       }
   }

}
