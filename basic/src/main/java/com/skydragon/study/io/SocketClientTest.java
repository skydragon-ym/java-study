package com.skydragon.study.io;

import java.io.*;
import java.net.Socket;

public class SocketClientTest {
    private static int CLIENT_SEND_BUFFER=10;
    private static boolean NO_DELAY = true;
    private static boolean CLIENT_OOB = false;

    public static void main(String[] args) throws IOException {
        Socket client = new Socket("192.168.75.130",9090);

        client.setSendBufferSize(CLIENT_SEND_BUFFER);
        //是否开启优化, true为不优化
        client.setTcpNoDelay(NO_DELAY);
        client.setOOBInline(CLIENT_OOB);

        OutputStream out = client.getOutputStream();
        InputStream in = System.in;
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        while(true){
            String line = reader.readLine();
            if(line != null){
                /*
                byte[] bytes = line.getBytes();
                for(byte b : bytes){
                    out.write(b);
                }*/
                out.write(line.getBytes());
            }
        }

    }

}
