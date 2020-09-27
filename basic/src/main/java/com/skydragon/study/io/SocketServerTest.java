package com.skydragon.study.io;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerTest {
    //超过BACK_LOG数量的请求将会被丢弃

    //server socket property
    private static int BACK_LOG=2;
    private static int SERVER_RECEIVE_BUFFER=10;
    private static int SERVER_SEND_BUFFER=10;
    private static boolean SERVER_REUSE_ADDR=false;
    private static int SERVER_TIMEOUT=0;

    //client socket property
    private static int CLIENT_RECEIVE_BUFFER=10;
    private static int CLIENT_SEND_BUFFER=10;
    private static boolean CLIENT_REUSE_ADDR=false;
    private static boolean CLIENT_KEEP_ALIVE = false;
    //是否首字节发送
    private static boolean CLIENT_OOB = false;
    private static boolean CLIENT_LINGER=true;
    private static int CLIENT_LINGER_NUMBER=1;

    public static void main(String[] args) throws IOException {
        ServerSocket server = null;

        //BIO
        try{
            server = new ServerSocket();
            server.bind(new InetSocketAddress(9090), BACK_LOG);
            server.setReceiveBufferSize(SERVER_RECEIVE_BUFFER);
            server.setReuseAddress(SERVER_REUSE_ADDR);
            server.setSoTimeout(SERVER_TIMEOUT);

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Server is running up on port 9090");

        while (true){
            try{
                System.in.read();

                Socket client = server.accept();
                System.out.println("client port: " + client.getPort());

                client.setKeepAlive(CLIENT_KEEP_ALIVE);
                client.setOOBInline(CLIENT_OOB);
                client.setReceiveBufferSize(CLIENT_RECEIVE_BUFFER);
                client.setReuseAddress(CLIENT_REUSE_ADDR);
                client.setSendBufferSize(CLIENT_SEND_BUFFER);
                client.setSoLinger(CLIENT_LINGER,CLIENT_LINGER_NUMBER);

                new Thread(()->{
                    while (true){
                        try {
                            InputStream inputStream = client.getInputStream();
                            //BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                            BufferedInputStream bis = new BufferedInputStream(inputStream);
                            byte[] data = new byte[1024];
                            int num = bis.read(data);
                            if(num>0){
                                String content = new String(data,0,num);
                                System.out.println("read client data is: " + num + "val:" + content);
                            }else if(num == 0){
                                System.out.println("client read nothing");
                                continue;
                            }else{
                                System.out.println("client read -1...");
                                client.close();
                                break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                server.close();
            }
        }
    }
}
