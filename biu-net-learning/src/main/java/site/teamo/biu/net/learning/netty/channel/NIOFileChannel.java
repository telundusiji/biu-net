package site.teamo.biu.net.learning.netty.channel;

import sun.nio.ch.FileChannelImpl;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/17
 */
public class NIOFileChannel {
    public static void main2(String[] args) throws IOException {
        String test = "爱做梦的锤子";

        FileOutputStream out = new FileOutputStream("./file.txt");

        FileChannel outChannel = out.getChannel();

        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

        writeBuffer.put(test.getBytes(Charset.defaultCharset()));

        writeBuffer.flip();

        outChannel.write(writeBuffer);

        out.close();

        FileInputStream in = new FileInputStream("./file.txt");

        FileChannel inChannel = in.getChannel();

        ByteBuffer readBuffer = ByteBuffer.allocate(1024);

        inChannel.read(readBuffer);

        readBuffer.flip();


        byte[] bytes = new byte[readBuffer.limit()];

        readBuffer.get(bytes);

        System.out.println(new String(bytes,Charset.defaultCharset()));

        in.close();

    }

    public static void main3(String[] args) throws Exception{

        FileInputStream in = new FileInputStream("./file.txt");
        FileChannel inChannel = in.getChannel();

        FileOutputStream out = new FileOutputStream("./file1.txt");
        FileChannel outChannel = out.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1);

        while (true){
            buffer.clear();
            int read = inChannel.read(buffer);
            if(read==-1){
                break;
            }
            buffer.flip();
            outChannel.write(buffer);
        }

        in.close();
        out.close();
    }

    public static void main4(String[] args) throws Exception{

        FileInputStream in = new FileInputStream("D:\\下载\\迅雷下载\\CentOS-7-x86_64-DVD-1708\\CentOS-7-x86_64-DVD-1708.iso");
        FileChannel inChannel = in.getChannel();

        FileOutputStream out = new FileOutputStream("D:\\下载\\迅雷下载\\CentOS-7-x86_64-DVD-1708\\CentOS.iso");
        FileChannel outChannel = out.getChannel();

        outChannel.transferFrom(inChannel,0,inChannel.size());

        in.close();
        out.close();
    }

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.putChar('a');
        buffer.putInt(257);
        System.out.println("");
        int a = 257;
        byte b = (byte)a;
    }
}
