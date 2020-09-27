package com.skydragon.study.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BufferedReaderTest {
    public static void main(String[] args) throws IOException {
        InputStream inputStream = System.in;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        while(true){
            String line = reader.readLine();
            if(line != null){
                System.out.println(line);
            }
        }
    }
}
