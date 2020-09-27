package com.skydragon.study.io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BufferedInputStreamTest {
    public static void main(String[] args) throws IOException {
        InputStream inputStream = System.in;
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        byte[] buffer = new byte[2];
        int bytesRead = 0;
        while (true){
            bytesRead = bis.read(buffer);
            String chunk = new String(buffer, 0, bytesRead);
            System.out.println(chunk);
        }
    }
}
