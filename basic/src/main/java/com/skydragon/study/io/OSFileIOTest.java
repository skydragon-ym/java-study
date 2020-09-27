package com.skydragon.study.io;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class OSFileIOTest {
    static byte[] data = "1234567890\n".getBytes();

    public static void basicFileIO(String filePath) throws IOException {
        File file = new File(filePath);

        //每10个字节就会做一次系统调用写入主存
        FileOutputStream os = new FileOutputStream(file);

        while(true){
            os.write(data);

            //注意这里我们不主动调用flush方法，由系统内核决定何时把数据写入磁盘
            //os.flush();
        }
    }

    public static void bufferedFileIO(String filePath) throws IOException {
        File file = new File(filePath);

        //BufferedOutputStream, JVM开辟8K空间，缓存写入的数据，8K写满后才做系统调用写入主存
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));

        while(true){
            bos.write(data);

            //注意这里我们不主动调用flush方法，由系统内核决定何时把数据写入磁盘
            //bos.flush();
        }
    }

    public static void randomAccessFileIO(String filePath) throws IOException, InterruptedException {
        File file = new File(filePath);

        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.write("hello world\n".getBytes());
        raf.write("hello Jeffrey\n".getBytes());
        System.out.println("random write done");
        System.in.read();

        raf.seek(6);
        raf.write("Java".getBytes());
        System.out.println("random seed/write done");
        System.in.read();

        //引入nio
        FileChannel channel = raf.getChannel();
        //只有文件类型（块设备）的Channel才有map方法, 因为mapped buffer直接映射到系统内核，就不再需要系统调用了
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE,0,4096);
        mappedByteBuffer.put(data);

        //注意这里我们不主动调用flush方法，由系统内核决定何时把数据写入磁盘
        //mappedByteBuffer.force();
        System.in.read();

        //usage of ByteBuffer
        raf.seek(0);
        ByteBuffer buffer = ByteBuffer.allocate(8192);
        //ByteBuffer buffer = ByteBuffer.allocateDirect(8192);

        channel.read(buffer);

        System.out.println(buffer);
        buffer.flip();
        System.out.println(buffer);

        for(int i=0; i<buffer.limit();i++){
            Thread.sleep(200);
            System.out.println((char)buffer.get(i));
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        switch(args[0]){
            case "1":
                basicFileIO(args[1]);
                break;
            case "2":
                bufferedFileIO(args[1]);
                break;
            case "3":
                randomAccessFileIO(args[1]);
                break;
            default:
        }
    }

}
